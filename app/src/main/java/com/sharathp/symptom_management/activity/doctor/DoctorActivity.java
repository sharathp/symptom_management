package com.sharathp.symptom_management.activity.doctor;

import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.SingleFragmentActivity;
import com.sharathp.symptom_management.fragment.doctor.DoctorFragment;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.service.PatientService;

public class DoctorActivity extends SingleFragmentActivity {

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        switch(id) {
            case R.id.doctor_logout:
                Session.clearSavedSession(this);
                logout();
                return true;
            case R.id.doctor_action_refresh_patients:
                final Intent intent = PatientService.createUpdatePatientsIntent(this);
                startService(intent);
                return true;
            case R.id.doctor_action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Fragment getFragment() {
        return new DoctorFragment();
    }
}
