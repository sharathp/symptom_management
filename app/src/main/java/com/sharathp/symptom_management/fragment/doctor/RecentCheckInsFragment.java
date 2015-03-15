package com.sharathp.symptom_management.fragment.doctor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

/**
 * Fragment to display recent check-ins of all the patients of this doctor.
 *
 * @author sharathp
 */
public class RecentCheckInsFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String TAG = RecentCheckInsFragment.class.getSimpleName();
    private static final int RECENTS_CHECKINS_LOADER_ID = 0;
    private static final int DEFAULT_RECENT_NUM_CHECKINS = 5;
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

    private void loadRecentCheckIns() {
        getLoaderManager().initLoader(RECENTS_CHECKINS_LOADER_ID, null, this);
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
            case RECENTS_CHECKINS_LOADER_ID:
                return new CursorLoader(getActivity(), getRecentCheckInsUri(),
                        NamedCheckInContract.NamedCheckInEntry.ALL_COLUMNS, null, null, null);
        }
        return null;
    }

    private Uri getRecentCheckInsUri() {
        final long doctorId =Session.restore(getActivity()).getId();
        return NamedCheckInContract.NamedCheckInEntry.buildRecentCheckInsUri(doctorId, getNumberOfRecentCheckIns());
    }

    private int getNumberOfRecentCheckIns() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt("num_recent_checkins", DEFAULT_RECENT_NUM_CHECKINS);
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