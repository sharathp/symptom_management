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

import com.sharathp.symptom_management.adapter.common.NamedCheckInListAdapter;
import com.sharathp.symptom_management.data.provider.contract.NamedCheckInContract;
import com.sharathp.symptom_management.fragment.common.BaseListFragment;
import com.sharathp.symptom_management.fragment.common.CheckInDetailsDialogFragment;
import com.sharathp.symptom_management.login.Session;

public class AllPatientsLastCheckinFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String TAG = AllPatientsLastCheckinFragment.class.getSimpleName();
    private static final int ALL_PATIENTS_LAST_CHECKIN_LOADER_ID = 0;
    private ListView mListView;

    private NamedCheckInListAdapter mNamedCheckInListAdapter;

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

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final CheckInDetailsDialogFragment fragment = CheckInDetailsDialogFragment.createInstance(id);
        fragment.show(getFragmentManager(), "checkin-details");
    }

    private void loadRecentCheckIns() {
        getLoaderManager().initLoader(ALL_PATIENTS_LAST_CHECKIN_LOADER_ID, null, this);
    }

    private void initializeListViewAndAdapter() {
        mNamedCheckInListAdapter = new NamedCheckInListAdapter(getActivity(), null, 0);
        setListAdapter(mNamedCheckInListAdapter);
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
            case ALL_PATIENTS_LAST_CHECKIN_LOADER_ID:
                final String sortOrder = NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_FIRST_NAME +
                        ", " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_LAST_NAME;
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), getAllPatientsLastCheckinUri(),
                        NamedCheckInContract.NamedCheckInEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    private Uri getAllPatientsLastCheckinUri() {
        final long doctorId = Session.restore(getActivity()).getId();
        return NamedCheckInContract.NamedCheckInEntry.buildAllPatientLastCheckinUri(doctorId);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mNamedCheckInListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mNamedCheckInListAdapter.swapCursor(null);
    }
}