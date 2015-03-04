package com.sharathp.symptom_management.activity.doctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.BaseActivity;
import com.sharathp.symptom_management.ui.DoctorDrawer;

/**
 * Base Activity for doctors with a drawer.
 */
public abstract class DoctorDrawerActivity extends BaseActivity {
    public static final String DRAWER_ITEM_ID_EXTRA = "doctor_drawer_item_id";
    private DoctorDrawer mDoctorDrawer;

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setDrawer();
    }

    private void setDrawer() {
        mDoctorDrawer = new DoctorDrawer(this, getToolbar());
        final int selectedDrawerItemId = getIntent().getIntExtra(DRAWER_ITEM_ID_EXTRA, -1);
        if(selectedDrawerItemId != -1) {
            mDoctorDrawer.setDrawerItemAsSelected(selectedDrawerItemId);
        }
    }
}