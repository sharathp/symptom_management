package com.sharathp.symptom_management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sharathp.symptom_management.login.Session;


public class MainActivity extends Activity {

    public static final int REQUEST_LOGIN = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session session = Session.restore(this);
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
            // TODO - Log an error and clear session?
        }
        finish();
    }

    private void launchPatientActivity() {
        // TODO - launch patient activity
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
                    // TODO - this shouldn't happen, log error and quit..
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
