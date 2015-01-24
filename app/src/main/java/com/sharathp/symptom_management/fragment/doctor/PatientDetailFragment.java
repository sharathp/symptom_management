package com.sharathp.symptom_management.fragment.doctor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.BaseFragment;
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
public class PatientDetailFragment extends BaseFragment {
    private static final String TAG = PatientDetailFragment.class.getSimpleName();
    public static final String ARG_PATIENT_ID = "patient_id";
    private static final int PATIENT_LOADER_ID = 0;

    @Inject
    SymptomManagementAPI mSymptomManagementAPI;

    @InjectView(R.id.first_name_text_view)
    TextView mTextView;

    private Patient mPatient;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        mTextView.setText(mPatient.getFirstName());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.d_fragment_patient_detail, container, false);
        return rootView;
    }
}