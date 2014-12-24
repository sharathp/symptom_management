package com.sharathp.symptom_management.activity;

import android.app.Fragment;

import com.sharathp.symptom_management.fragment.LoginFragment;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new LoginFragment();
    }
}