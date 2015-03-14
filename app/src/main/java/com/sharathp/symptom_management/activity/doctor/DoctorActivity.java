package com.sharathp.symptom_management.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.common.SettingsActivity;
import com.sharathp.symptom_management.fragment.doctor.DoctorFragment;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.service.PatientService;
import com.sharathp.symptom_management.ui.DoctorDrawer;

public class DoctorActivity extends DoctorDrawerActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.c_activity_single;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fragment fragment = new DoctorFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected int getDrawerItemPosition() {
        return DoctorDrawer.HOME_ID;
    }

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
                final String doctorServerId = Session.restore(this).getServerId();
                final Intent intent = PatientService.createUpdatePatientsIntent(this, doctorServerId);
                startService(intent);
                return true;
            case R.id.doctor_action_settings:
                final Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}