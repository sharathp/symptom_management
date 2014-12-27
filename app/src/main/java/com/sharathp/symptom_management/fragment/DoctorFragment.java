package com.sharathp.symptom_management.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.login.Session;

/**
 * Doctor {@link android.app.Fragment}.
 */
public class DoctorFragment extends BaseFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_doctor, container, false);
        final TextView welcomeView = (TextView) rootView.findViewById(R.id.doctor_welcome_text);
        welcomeView.setText("Hello " + Session.restore(getActivity()).getUserName());
        return rootView;
    }
}
