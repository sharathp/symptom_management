package com.sharathp.symptom_management.fragment.patient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.common.BaseFragment;

/**
 * Patient {@link Fragment}.
 */
public class PatientFragment extends BaseFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.p_fragment_patient, container, false);
        return rootView;
    }
}
