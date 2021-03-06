package com.sharathp.symptom_management.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.common.BaseActivity;
import com.sharathp.symptom_management.fragment.doctor.PatientAllDetailsFragment;
import com.sharathp.symptom_management.fragment.doctor.PatientListFragment;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.service.PatientService;

/**
 * An activity representing a list of Patients. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PatientDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.sharathp.symptom_management.fragment.doctor.PatientListFragment} and the item details
 * (if present) is a {@link com.sharathp.symptom_management.fragment.doctor.PatientAllDetailsFragment}.
 * <p/>
 * This activity also implements the required
 * {@link com.sharathp.symptom_management.fragment.doctor.PatientListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PatientListActivity extends BaseActivity
        implements PatientListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected int getLayoutResource() {
        return R.layout.d_activity_patient_list;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.patient_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((PatientListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.patient_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
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
        switch (id) {
            case R.id.doctor_logout:
                Session.clearSavedSession(this);
                logout();
                return true;
            case R.id.doctor_action_refresh_patients:
                final String doctorUserId = Session.restore(this).getServerId();
                final Intent intent = PatientService.createUpdatePatientsIntent(this, doctorUserId);
                startService(intent);
                return true;
            case R.id.doctor_action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback method from {@link PatientListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(final long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            final Bundle arguments = new Bundle();
            arguments.putLong(PatientAllDetailsFragment.ARG_PATIENT_ID, id);

            final PatientAllDetailsFragment fragment = new PatientAllDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patient_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            final Intent detailIntent = new Intent(this, PatientDetailActivity.class);
            detailIntent.putExtra(PatientAllDetailsFragment.ARG_PATIENT_ID, id);
            startActivity(detailIntent);
        }
    }
}
