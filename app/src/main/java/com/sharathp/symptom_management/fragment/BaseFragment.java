package com.sharathp.symptom_management.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.sharathp.symptom_management.app.SymptomManagementApplication;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SymptomManagementApplication) getActivity().getApplication()).inject(this);
    }
}
