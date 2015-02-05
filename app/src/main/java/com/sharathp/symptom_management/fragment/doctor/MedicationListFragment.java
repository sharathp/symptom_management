package com.sharathp.symptom_management.fragment.doctor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.fragment.BaseListFragment;
import com.sharathp.symptom_management.model.Medication;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A list fragment representing a list of Medications.
 */
public class MedicationListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MedicationListFragment.class.getSimpleName();
    private static final int MEDICATIONS_LOADER_ID = 0;
    private ListView mListView;
    private MedicationListAdapter mMedicationListAdapter;

    private long mPatientId;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeListViewAndAdapter();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey(PatientAllDetailsFragment.ARG_PATIENT_ID)) {
            mPatientId = getArguments().getLong(PatientAllDetailsFragment.ARG_PATIENT_ID);
            loadMedications();
        }
    }

    private void loadMedications() {
        getLoaderManager().initLoader(MEDICATIONS_LOADER_ID, null, this);
    }

    private void initializeListViewAndAdapter() {
        mMedicationListAdapter = new MedicationListAdapter(getActivity(), null, 0);
        setListAdapter(mMedicationListAdapter);
        // this is required as setting list adapter above, shows the list view
        setListShown(false);

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                final Cursor cursor = mMedicationListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    final Medication medication = PatientContract.PatientMedicationEntry.readMedication(cursor);
                    Toast.makeText(getActivity(), medication.getName(), Toast.LENGTH_LONG);
                }
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case MEDICATIONS_LOADER_ID:
                final String sortOrder = PatientContract.PatientMedicationEntry.COLUMN_NAME + " ASC";
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), getPatientMedicationsUri(),
                        PatientContract.PatientMedicationEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    private Uri getPatientMedicationsUri() {
        return PatientContract.PatientMedicationEntry.buildPatientMedicationsUri(mPatientId);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mMedicationListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mMedicationListAdapter.swapCursor(null);
    }

    // nested classes and interfaces..
    static class MedicationListAdapter extends CursorAdapter {

        MedicationListAdapter(final Context context, final Cursor c, final int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
            final View view = LayoutInflater.from(context).inflate(R.layout.list_item_medication, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            final Medication medication = PatientContract.PatientMedicationEntry.readMedication(cursor);
            viewHolder.mNameTextView.setText(medication.getName());
            viewHolder.mServerIdTextView.setText(medication.getServerId());
        }
    }

    static class ViewHolder {
        @InjectView(R.id.name_text_view)
        TextView mNameTextView;

        @InjectView(R.id.server_id_text_view)
        TextView mServerIdTextView;

        ViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }
}
