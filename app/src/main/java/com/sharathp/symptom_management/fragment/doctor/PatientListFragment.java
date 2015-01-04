package com.sharathp.symptom_management.fragment.doctor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sharathp.symptom_management.fragment.BaseListFragment;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.loader.Callback;
import com.sharathp.symptom_management.loader.RetrofitLoader;
import com.sharathp.symptom_management.loader.RetrofitLoaderUtil;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.Patient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A list fragment representing a list of Patients. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PatientDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PatientListFragment extends BaseListFragment {
    private static final String TAG = PatientListFragment.class.getSimpleName();
    private static final int PATIENTS_LOADER_ID = 0;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    @Inject
    SymptomManagementAPI mSymptomManagementAPI;

    private List<Patient> mPatients;

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
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientListFragment() {
        // no-op
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapater(new ArrayList<Patient>());
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        loadPatients();
    }

    private void loadPatients() {
        RetrofitLoaderUtil.init(getLoaderManager(), PATIENTS_LOADER_ID,
            new RetrofitLoader<List<Patient>, SymptomManagementAPI>(getActivity(), mSymptomManagementAPI) {
                @Override
                public List<Patient> call(final SymptomManagementAPI symptomManagementAPI) {
                    final String doctorId = Session.restore(getActivity()).getUserId();
                    return symptomManagementAPI.getPatientsForDoctor(doctorId);
                }
            },
            new Callback<List<Patient>>() {
                @Override
                public void onSuccess(final List<Patient> result) {
                    setListAdapater(result);
                }

                @Override
                public void onFailure(final Exception e) {
                    Timber.d(TAG, "Unable to retrieve mPatients list: " + e.getMessage());
                    //TODO - find a way to logout
                }
            });
    }

    private void setListAdapater(final List<Patient> patients) {
        this.mPatients = patients;
        setListAdapter(new ArrayAdapter<Patient>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                patients));
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
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
    public void onListItemClick(final ListView listView, final View view, final int position,
                                final long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(mPatients.get(position).getId());
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
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
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(final int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

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
}
