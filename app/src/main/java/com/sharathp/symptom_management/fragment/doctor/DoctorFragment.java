package com.sharathp.symptom_management.fragment.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.doctor.PatientListActivity;
import com.sharathp.symptom_management.fragment.BaseFragment;
import com.sharathp.symptom_management.login.Session;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Doctor {@link android.app.Fragment}.
 */
public class DoctorFragment extends BaseFragment {

    @InjectView(R.id.doctor_welcome_text)
    TextView mWelcomeView;

    @OnClick(R.id.my_patients_button)
    void viewMyPatients() {
        final Intent intent = new Intent(getActivity(), PatientListActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_fragment_doctor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWelcomeView.setText("Hello " + Session.restore(getActivity()).getUserName());
    }
}
