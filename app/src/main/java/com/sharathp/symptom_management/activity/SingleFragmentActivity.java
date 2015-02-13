package com.sharathp.symptom_management.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sharathp.symptom_management.R;

/**
 * {@link android.support.v4.app.FragmentActivity} base class for single fragment activities.
 */
public abstract class SingleFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fragment fragment = getFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.c_activity_single;
    }

    protected abstract Fragment getFragment();
}
