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

import com.sharathp.symptom_management.adapter.common.PatientCheckInListAdapter;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.data.provider.contract.RecentCheckInContract;
import com.sharathp.symptom_management.fragment.BaseListFragment;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.PatientCheckIn;

/**
 * Fragment to display most recent check-ins of all the patients of this doctor.
 *
 * @author sharathp
 */
public class MostRecentCheckInsFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String TAG = MostRecentCheckInsFragment.class.getSimpleName();
    private static final int RECENTS_CHECKINS_LOADER_ID = 0;
    private ListView mListView;

    private PatientCheckInListAdapter mPatientCheckInListAdapter;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeListViewAndAdapter();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadRecentCheckIns();
    }

    private void loadRecentCheckIns() {
        getLoaderManager().initLoader(RECENTS_CHECKINS_LOADER_ID, null, this);
    }

    private void initializeListViewAndAdapter() {
        mPatientCheckInListAdapter = new PatientCheckInListAdapter(getActivity(), null, 0);
        setListAdapter(mPatientCheckInListAdapter);
        // this is required as setting list adapter above, shows the list view
        setListShown(false);

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                final Cursor cursor = mPatientCheckInListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    // TODO - FIXME
                    final PatientCheckIn patientCheckIn = PatientCheckInContract.PatientCheckInEntry.readPatientCheckIn(cursor);
                    Toast.makeText(getActivity(), patientCheckIn.getCheckinTime().toGMTString(), Toast.LENGTH_LONG);
                }
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case RECENTS_CHECKINS_LOADER_ID:
                final String sortOrder = PatientCheckInContract.PatientCheckInEntry.COLUMN_CHECKIN_TIME + " ASC";
                // TODO - FIXME
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), getRecentCheckInsUri(),
                        RecentCheckInContract.RecentCheckInEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    private Uri getRecentCheckInsUri() {
        final long doctorId =Session.restore(getActivity()).getId();
        return RecentCheckInContract.RecentCheckInEntry.buildRecentCheckInsUri(doctorId);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mPatientCheckInListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mPatientCheckInListAdapter.swapCursor(null);
    }
}