package com.sharathp.symptom_management.fragment.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.sharathp.symptom_management.activity.common.BaseActivity;

import butterknife.ButterKnife;

/**
 * Base ListFragment which performs injection using the activity object graph of its parent.
 */
public abstract class BaseListFragment extends ListFragment {

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        injectDependencies();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    private void injectDependencies() {
        ((BaseActivity) getActivity()).inject(this);
    }

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private void injectViews(final View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
