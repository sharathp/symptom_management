package com.sharathp.symptom_management.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.sharathp.symptom_management.activity.SingleFragmentActivity;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        injectDependencies();
    }

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    private void injectDependencies() {
        ((SingleFragmentActivity) getActivity()).inject(this);
    }
}
