package com.sharathp.symptom_management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sharathp.symptom_management.login.Session;

import timber.log.Timber;

/**
 * Entry point activity that "redirects" to login page if there is no session. or
 * to the proper activity if there is an existing session.
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_LOGIN = 99;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Session session = Session.restore(this);
        if (null == session) {
            launchLoginActivity();
        } else {
            launchUserActivity(session);
        }
    }

    private void launchLoginActivity() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
    }

    private void launchUserActivity(final Session session) {
        if(session.isDoctor()) {
            launchDoctorActivity();
        } else if(session.isPatient()) {
            launchPatientActivity();
        } else {
            Timber.e(TAG, "Invalid session, user-type: " + session.getUserType());
        }
        finish();
    }

    private void launchPatientActivity() {
        startActivity(new Intent(this, PatientActivity.class));
    }

    private void launchDoctorActivity() {
        startActivity(new Intent(this, DoctorActivity.class));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                final Session session = Session.restore(this);
                if (resultCode == RESULT_OK && null != session) {
                    launchUserActivity(session);
                } else {
                    Timber.e(TAG, "Unsuccessful login, result-code: " + resultCode);
                }
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
