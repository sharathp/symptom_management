package com.sharathp.symptom_management.fragment.doctor;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.PatientContract;
import com.sharathp.symptom_management.fragment.BaseListFragment;
import com.sharathp.symptom_management.model.Patient;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A list fragment representing a list of Patients. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PatientDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PatientListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PatientListFragment.class.getSimpleName();
    private static final int PATIENTS_LOADER_ID = 0;
    private ListView mListView;
    private PatientListAdapter mPatientListAdapter;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            mActivatedPosition = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeListViewAndAdapter();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        getLoaderManager().initLoader(PATIENTS_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(final boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mListView.setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void initializeListViewAndAdapter() {
        mPatientListAdapter = new PatientListAdapter(getActivity(), null, 0);
        setListAdapter(mPatientListAdapter);
        // this is required as setting list adapter above, shows the list view
        setListShown(false);

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                final Cursor cursor = mPatientListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    final Patient patient = PatientContract.readPatient(cursor);
                    mCallbacks.onItemSelected(patient.getId());
                    mActivatedPosition = position;
                }
            }
        });
    }

    private void setActivatedPosition() {
        if (mActivatedPosition == ListView.INVALID_POSITION) {
            mListView.setItemChecked(mActivatedPosition, false);
        } else {
            mListView.setItemChecked(mActivatedPosition, true);
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mActivatedPosition);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case PATIENTS_LOADER_ID:
                final String sortOrder = PatientContract.PatientEntry.COLUMN_FIRST_NAME + " ASC";
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), PatientContract.PatientEntry.CONTENT_URI,
                        PatientContract.PatientEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mPatientListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
        cursor.setNotificationUri(getActivity().getContentResolver(), PatientContract.PatientEntry.CONTENT_URI);
        setActivatedPosition();
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mPatientListAdapter.swapCursor(null);
    }

    // nested classes and interfaces..

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
            // no-op
        }
    };

    static class PatientListAdapter extends CursorAdapter {

        PatientListAdapter(final Context context, final Cursor c, final int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
            final View view = LayoutInflater.from(context).inflate(R.layout.d_list_item_patient, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            final Patient patient = PatientContract.readPatient(cursor);
            viewHolder.mNameView.setText(patient.getFirstName() + " " + patient.getLastName());
            viewHolder.mPatientIdView.setText(patient.getPatientId());
        }
    }

    static class ViewHolder {
        @InjectView(R.id.nameView)
        TextView mNameView;

        @InjectView(R.id.patientIdView)
        TextView mPatientIdView;

        ViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }
}
