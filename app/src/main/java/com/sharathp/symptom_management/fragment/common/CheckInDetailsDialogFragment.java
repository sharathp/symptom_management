package com.sharathp.symptom_management.fragment.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.adapter.common.CheckInUtils;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.model.MedicationIntake;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckInDetailsDialogFragment extends DialogFragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CHECKIN_LOADER_ID = 0;
    private static final int CHECKIN_MEDICATIONS_LOADER_ID = 1;

    private static final String CHECKIN_ID_ARG = "checkinId";

    @InjectView(R.id.checkin_medications_table)
    TableLayout mMedicationsTable;

    @InjectView(R.id.checkin_pain_text_view)
    TextView mPainTextView;

    @InjectView(R.id.pain_image_view)
    ImageView mPainImageView;

    @InjectView(R.id.checkin_eating_text_view)
    TextView mEatingTextView;

    @InjectView(R.id.eat_image_view)
    ImageView mEatingImageView;

    private View rootView;

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
        rootView = inflater.inflate(R.layout.c_fragment_checkin_details, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // FIXME - Refactor title and button text to string resources..
        builder.setView(rootView)
               .setTitle("Checkin Details")
               .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(final DialogInterface dialog, final int which) {
                       dialog.dismiss();
                   }
               });

        ButterKnife.inject(this, rootView);

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
                //TODO - should we move this cursor traversal to readPatientCheckIn()??
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
        mEatingTextView.setText(patientCheckIn.getEating().name());
        mEatingImageView.setImageResource(CheckInUtils.getEatImage(patientCheckIn.getEating()));
        mPainTextView.setText(patientCheckIn.getPain().name());
        mPainImageView.setImageResource(CheckInUtils.getPainImage(patientCheckIn.getPain()));

        final String title = getResources().getString(R.string.checkin_detail_time_format,
                CheckInUtils.getDateString(patientCheckIn.getCheckinTime(), getActivity()));
        getDialog().setTitle(title);
    }

    private void populateCheckInMedications(final List<MedicationIntake> medicationsIntake) {
        if (medicationsIntake == null || medicationsIntake.isEmpty()) {
            final TableRow tableRow = new TableRow(getActivity());
            tableRow.addView(getTextViewInstance("-None-"));

            mMedicationsTable.addView(tableRow);
            return;
        }

        for (final MedicationIntake medicationIntake: medicationsIntake) {
            final TableRow tableRow = new TableRow(getActivity());
            tableRow.addView(getMedicationView(medicationIntake.getMedication().getName()));
            // TODO - remove this?
            // tableRow.addView(getMedicationTimeView(medicationIntake.getTime()));
            mMedicationsTable.addView(tableRow);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // check-in is immutable, so, no-op..
    }


    private View getMedicationView(final String medicationName) {
        return getTextViewInstance(medicationName);
    }

    private View getMedicationTimeView(final Date time) {
        return getTextViewInstance(CheckInUtils.getDateString(time, getActivity()).toString());
    }

    private TextView getTextViewInstance(final String text) {
        final TextView textView = new TextView(getActivity());
        textView.setText(text);
        return textView;
    }
}