package com.sharathp.symptom_management.fragment.doctor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sharathp.symptom_management.adapter.common.CheckInListAdapter;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.fragment.common.BaseListFragment;
import com.sharathp.symptom_management.fragment.common.CheckInDetailsDialogFragment;
import com.sharathp.symptom_management.model.PatientCheckIn;

/**
 * A list fragment representing a list of patient check-ins.
 */
public class PatientCheckInListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PatientCheckInListFragment.class.getSimpleName();
    private static final int PATIENT_CHECKINS_LOADER_ID = 0;
    private ListView mListView;

    private CheckInListAdapter mCheckInListAdapter;

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
            loadPatientCheckIns();
        }
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final CheckInDetailsDialogFragment fragment = CheckInDetailsDialogFragment.createInstance(id);
        fragment.show(getFragmentManager(), "checkin-details");
    }

    private void loadPatientCheckIns() {
        getLoaderManager().initLoader(PATIENT_CHECKINS_LOADER_ID, null, this);
    }

    private void initializeListViewAndAdapter() {
        mCheckInListAdapter = new CheckInListAdapter(getActivity(), null, 0);
        setListAdapter(mCheckInListAdapter);
        // this is required as setting list adapter above, shows the list view
        setListShown(false);

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
                final CheckInDetailsDialogFragment fragment = CheckInDetailsDialogFragment.createInstance(id);
                fragment.show(getFragmentManager(), "checkin-details");
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case PATIENT_CHECKINS_LOADER_ID:
                final String sortOrder = PatientCheckInContract.PatientCheckInEntry.COLUMN_CHECKIN_TIME + " DESC";
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), getPatientPatientCheckInsUri(),
                        PatientCheckInContract.PatientCheckInEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    private Uri getPatientPatientCheckInsUri() {
        return PatientCheckInContract.PatientCheckInEntry.buildPatientPatientCheckInsUri(mPatientId);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mCheckInListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mCheckInListAdapter.swapCursor(null);
    }
}