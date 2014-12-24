package com.sharathp.symptom_management.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.sharathp.symptom_management.R;

/**
 * {@link Activity} base class for single fragment activities.
 */
public abstract class SingleFragmentActivity extends Activity {
    private static final String TAG = SingleFragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        final Fragment fragment = getFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    protected abstract Fragment getFragment();
}
