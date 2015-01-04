package com.sharathp.symptom_management.fragment.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.BaseFragment;
import com.sharathp.symptom_management.login.Session;

import butterknife.InjectView;

/**
 * Doctor {@link android.app.Fragment}.
 */
public class DoctorFragment extends BaseFragment {

    @InjectView(R.id.doctor_welcome_text)
    TextView mWelcomeView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.d_fragment_doctor, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWelcomeView.setText("Hello " + Session.restore(getActivity()).getUserName());
    }
}
