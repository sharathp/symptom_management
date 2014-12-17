package com.sharathp.symptom_management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sharathp.symptom_management.login.Session;


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
            Log.e(TAG, "Invalid session, user-type: " + session.getUserType());
        }
        finish();
    }

    private void launchPatientActivity() {
        startActivity(new Intent(this, PatientActivity.class));
    }

    private void launchDoctorActivity() {
        // TODO - launch doctor activity
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                final Session session = Session.restore(this);
                if (resultCode == RESULT_OK && null != session) {
                    launchUserActivity(session);
                } else {
                    Log.e(TAG, "Unsuccessful login, result-code: " + resultCode);
                }
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
