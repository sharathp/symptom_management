package com.sharathp.symptom_management.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.LoginFragment;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected boolean includeDrawer() {
        return false;
    }

    @Override
    protected Fragment getFragment() {
        return new LoginFragment();
    }
}