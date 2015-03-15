package com.sharathp.symptom_management.fragment.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.adapter.common.CheckInUtils;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.model.MedicationIntake;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckInDetailsDialogFragment extends DialogFragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CHECKIN_LOADER_ID = 0;
    private static final int CHECKIN_MEDICATIONS_LOADER_ID = 1;

    private static final String CHECKIN_ID_ARG = "checkinId";

    @InjectView(R.id.checkin_medications_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.checkin_time_text_view)
    TextView mCheckinTimeTextView;

    @InjectView(R.id.checkin_pain_text_view)
    TextView mPainTextView;

    @InjectView(R.id.pain_image_view)
    ImageView mPainImageView;

    @InjectView(R.id.checkin_eating_text_view)
    TextView mEatingTextView;

    @InjectView(R.id.eat_image_view)
    ImageView mEatingImageView;

    private long mCheckinId;

    public static CheckInDetailsDialogFragment createInstance(final long checkinId) {
        final Bundle args = new Bundle();
        args.putLong(CHECKIN_ID_ARG, checkinId);
        final CheckInDetailsDialogFragment fragment = new CheckInDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckinId = getArguments().getLong(CHECKIN_ID_ARG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.c_fragment_checkin_details, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // FIXME - Refactor title and button text to string resources..
        builder.setView(view)
               .setTitle("Checkin Details")
               .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(final DialogInterface dialog, final int which) {
                       dialog.dismiss();
                   }
               });

        ButterKnife.inject(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        retrieveCheckInData();

        return builder.create();
    }

    private void retrieveCheckInData() {
        getLoaderManager().initLoader(CHECKIN_LOADER_ID, null, this);
        getLoaderManager().initLoader(CHECKIN_MEDICATIONS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case CHECKIN_LOADER_ID:
                final Uri checkinUri = PatientCheckInContract.PatientCheckInEntry
                        .buildPatientCheckInUri(mCheckinId);
                return new CursorLoader(getActivity(), checkinUri,
                        PatientCheckInContract.PatientCheckInEntry.ALL_COLUMNS,
                        null, null, null);
            case CHECKIN_MEDICATIONS_LOADER_ID:
                final Uri medicationsUri = PatientCheckInContract.PatientCheckInMedicationIntakeEntry
                                                        .buildCheckInMedicationsUri(mCheckinId);
                return new CursorLoader(getActivity(), medicationsUri,
                        PatientCheckInContract.PatientCheckInMedicationIntakeEntry.ALL_COLUMNS,
                        null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        switch (loader.getId()) {
            case CHECKIN_LOADER_ID:
                //TODO - should we move this to readPatientCheckIn()??
                cursor.moveToNext();
                final PatientCheckIn patientCheckIn = PatientCheckInContract.PatientCheckInEntry.readPatientCheckIn(cursor);
                populateCheckInData(patientCheckIn);
                break;
            case CHECKIN_MEDICATIONS_LOADER_ID:
                final  List<MedicationIntake> medicationIntakes = getCheckInMedications(cursor);
                populateCheckInMedications(medicationIntakes);
                break;
        }
    }

    private List<MedicationIntake> getCheckInMedications(final Cursor cursor) {
        if (cursor == null) {
            return Collections.emptyList();
        }

        final List<MedicationIntake> medicationsIntake = new ArrayList<>();

        while (cursor.moveToNext()) {
            final MedicationIntake medicationIntake = PatientCheckInContract.PatientCheckInMedicationIntakeEntry.readMedicationIntake(cursor);
            medicationsIntake.add(medicationIntake);
        }
        return medicationsIntake;
    }

    private void populateCheckInData(final PatientCheckIn patientCheckIn) {
        mCheckinTimeTextView.setText(CheckInUtils.getDateString(patientCheckIn.getCheckinTime(), getActivity()));
        mEatingTextView.setText(patientCheckIn.getEating().name());
        mEatingImageView.setImageResource(CheckInUtils.getEatImage(patientCheckIn.getEating()));
        mPainTextView.setText(patientCheckIn.getPain().name());
        mPainImageView.setImageResource(CheckInUtils.getPainImage(patientCheckIn.getPain()));
    }

    private void populateCheckInMedications(final List<MedicationIntake> medicationsIntake) {
        final MedicationsAdapter medicationsAdapter = new MedicationsAdapter(medicationsIntake, getActivity());
        mRecyclerView.setAdapter(medicationsAdapter);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // check-in is immutable, so, no-op..
    }

    static class MedicationsAdapter extends RecyclerView.Adapter<MedicationIntakeViewHolder> {
        private final List<MedicationIntake> mMedicationsIntake;
        private final WeakReference<Context> mContext;

        MedicationsAdapter(final List<MedicationIntake> medicationsIntake, final Context context) {
            this.mMedicationsIntake = medicationsIntake;
            this.mContext = new WeakReference<Context>(context);
        }

        @Override
        public MedicationIntakeViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_2, null);
            final MedicationIntakeViewHolder medicationIntakeViewHolder = new MedicationIntakeViewHolder(view);
            return medicationIntakeViewHolder;
        }

        @Override
        public void onBindViewHolder(final MedicationIntakeViewHolder medicationViewHolder, final int i) {
            final MedicationIntake medicationIntake = mMedicationsIntake.get(i);
            if (medicationIntake == null) {
                return;
            }
            medicationViewHolder.mMedicationNameTextView.setText(medicationIntake.getMedication().getName());
            medicationViewHolder.mMedicationTimeTextView.setText(CheckInUtils.getDateString(medicationIntake.getTime(), mContext.get()));
        }

        @Override
        public int getItemCount() {
            return (null != mMedicationsIntake ? mMedicationsIntake.size() : 0);
        }
    }

    static class MedicationIntakeViewHolder extends RecyclerView.ViewHolder {

        @InjectView(android.R.id.text1)
        TextView mMedicationNameTextView;

        @InjectView(android.R.id.text2)
        TextView mMedicationTimeTextView;

        MedicationIntakeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}