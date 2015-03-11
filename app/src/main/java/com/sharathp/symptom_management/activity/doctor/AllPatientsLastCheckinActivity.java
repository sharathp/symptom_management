package com.sharathp.symptom_management.activity.doctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.doctor.AllPatientsLastCheckinFragment;
import com.sharathp.symptom_management.ui.DoctorDrawer;

public class AllPatientsLastCheckinActivity extends DoctorDrawerActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.c_activity_single;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fragment fragment = new AllPatientsLastCheckinFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected int getDrawerItemPosition() {
        return DoctorDrawer.ALL_PATIENTS_LAST_CHECKIN_ID;
    }
}