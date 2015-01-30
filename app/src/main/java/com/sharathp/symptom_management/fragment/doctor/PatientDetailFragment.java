package com.sharathp.symptom_management.fragment.doctor;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.contract.PatientContract;
import com.sharathp.symptom_management.fragment.BaseFragment;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.model.Patient;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link com.sharathp.symptom_management.activity.doctor.PatientListActivity}
 * in two-pane mode (on tablets) or a {@link com.sharathp.symptom_management.activity.doctor.PatientDetailActivity}
 * on handsets.
 */
public class PatientDetailFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PatientDetailFragment.class.getSimpleName();
    public static final String ARG_PATIENT_ID = "patient_id";
    private static final int PATIENT_LOADER_ID = 0;

    @Inject
    SymptomManagementAPI mSymptomManagementAPI;

    @InjectView(R.id.first_name_text_view)
    TextView mTextView;

    private long mPatient_id;
    private Patient mPatient;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey(ARG_PATIENT_ID)) {
            mPatient_id = getArguments().getLong(ARG_PATIENT_ID);
            loadPatient();
        }
    }

    private void loadPatient() {
        getLoaderManager().initLoader(PATIENT_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.d_fragment_patient_detail, container, false);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case PATIENT_LOADER_ID:
                final Uri patientUri = PatientContract.PatientEntry.buildPatientUri(mPatient_id);
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), patientUri,
                        PatientContract.PatientEntry.ALL_COLUMNS, null, null, null);
                return cursorLoader;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }
        final Patient patient = PatientContract.readPatient(cursor);
        displayPatient(patient);
    }

    private void displayPatient(final Patient patient) {
        if(patient == null) {
            return;
        }
        mPatient = patient;
        mTextView.setText(mPatient.getFirstName());
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // no-op
    }
}