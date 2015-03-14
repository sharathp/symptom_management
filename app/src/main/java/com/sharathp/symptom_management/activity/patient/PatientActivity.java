package com.sharathp.symptom_management.activity.patient;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.common.SingleFragmentActivity;
import com.sharathp.symptom_management.fragment.patient.PatientFragment;
import com.sharathp.symptom_management.login.Session;


public class PatientActivity extends SingleFragmentActivity {

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        switch(id) {
            case R.id.patient_logout:
                Session.clearSavedSession(this);
                logout();
                return true;
            case R.id.patient_action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Fragment getFragment() {
        return new PatientFragment();
    }
}
