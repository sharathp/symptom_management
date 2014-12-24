package com.sharathp.symptom_management.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharathp.symptom_management.R;

/**
 * Patient {@link Fragment}.
 */
public class PatientFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_patient, container, false);
        return rootView;
    }
}
