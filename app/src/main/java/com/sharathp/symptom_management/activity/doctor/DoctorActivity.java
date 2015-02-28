package com.sharathp.symptom_management.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.SettingsActivity;
import com.sharathp.symptom_management.activity.SingleFragmentActivity;
import com.sharathp.symptom_management.fragment.doctor.DoctorFragment;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.service.PatientService;

public class DoctorActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawer();
    }

    private void setDrawer() {
        final int homeId = 1;
        final int settingsId = 2;
        final int aboutId = 3;
        final int rateId = 4;

        new Drawer()
            .withActivity(this)
            .withToolbar(getToolbar())
            .withHeader()
            .addDrawerItems(
                    new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(R.drawable.ic_home).withIdentifier(homeId),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_settings).withIdentifier(settingsId),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_info).withIdentifier(aboutId),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_rate).withIcon(R.drawable.ic_thumb_up).withIdentifier(rateId)
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id, final IDrawerItem drawerItem) {
                    switch (drawerItem.getIdentifier()) {
                        case homeId:
                            // TODO - go home, clear current task
                            break;
                        case settingsId:
                            // TODO - go to settings
                            break;
                        case aboutId:
                            // TODO - go to about
                            break;
                        case rateId:
                            // TODO - go to rate app in play store
                            break;
                    }
                    Toast.makeText(DoctorActivity.this, "Clicked: " + drawerItem.getIdentifier(), Toast.LENGTH_LONG).show();
                }
            })
            .build();
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

    @Override
    protected Fragment getFragment() {
        return new DoctorFragment();
    }
}
