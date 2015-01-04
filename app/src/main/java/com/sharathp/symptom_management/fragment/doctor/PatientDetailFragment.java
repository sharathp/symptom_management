package com.sharathp.symptom_management.fragment.doctor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.loader.Callback;
import com.sharathp.symptom_management.loader.RetrofitLoader;
import com.sharathp.symptom_management.loader.RetrofitLoaderUtil;
import com.sharathp.symptom_management.model.Patient;

import javax.inject.Inject;

import butterknife.InjectView;
import timber.log.Timber;

/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link com.sharathp.symptom_management.activity.doctor.PatientListActivity}
 * in two-pane mode (on tablets) or a {@link com.sharathp.symptom_management.activity.doctor.PatientDetailActivity}
 * on handsets.
 */
public class PatientDetailFragment extends Fragment {
    private static final String TAG = PatientDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PATIENT_ID = "patient_id";
    private static final int PATIENT_LOADER_ID = 0;

    @Inject
    SymptomManagementAPI mSymptomManagementAPI;

    @InjectView(R.id.patient_detail)
    TextView mTextView;

    private Patient mPatient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientDetailFragment() {
        // no-op
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PATIENT_ID)) {
            loadPatient(getArguments().getString(ARG_PATIENT_ID));
        }
    }

    private void loadPatient(final String patientId) {
        RetrofitLoaderUtil.init(getLoaderManager(), PATIENT_LOADER_ID,
                new RetrofitLoader<Patient, SymptomManagementAPI>(getActivity(), mSymptomManagementAPI) {
                    @Override
                    public Patient call(final SymptomManagementAPI symptomManagementAPI) {
                        return symptomManagementAPI.getPatientById(patientId);
                    }
                },
                new Callback<Patient>() {
                    @Override
                    public void onSuccess(final Patient result) {
                        displayPatient(result);
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        Timber.d(TAG, "Unable to retrieve patient: " + e.getMessage());
                        //TODO - find a way to logout
                    }
                });
    }

    private void displayPatient(final Patient patient) {
        if(patient == null) {
            return;
        }
        mPatient = patient;
        mTextView.setText(mPatient.getId());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.d_fragment_patient_detail, container, false);
        return rootView;
    }
}