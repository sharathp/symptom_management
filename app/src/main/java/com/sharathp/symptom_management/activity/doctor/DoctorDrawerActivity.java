package com.sharathp.symptom_management.activity.doctor;

import android.os.Bundle;

import com.sharathp.symptom_management.activity.common.BaseActivity;
import com.sharathp.symptom_management.ui.DoctorDrawer;

/**
 * Base Activity for doctors with a drawer.
 */
public abstract class DoctorDrawerActivity extends BaseActivity {
    private DoctorDrawer mDoctorDrawer;

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedDrawerItem();
    }

    private void setDrawer() {
        mDoctorDrawer = new DoctorDrawer(this, getToolbar());
        setSelectedDrawerItem();
    }

    private void setSelectedDrawerItem() {
        final int selectedDrawerItemId = getDrawerItemPosition();
        if(selectedDrawerItemId != -1) {
            mDoctorDrawer.setDrawerItemAsSelected(selectedDrawerItemId);
        }
    }

    protected int getDrawerItemPosition() {
        return -1;
    }
}