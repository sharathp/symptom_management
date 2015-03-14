package com.sharathp.symptom_management.fragment.doctor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.fragment.common.BaseFragment;
import com.sharathp.symptom_management.model.Patient;

import butterknife.InjectView;

public class PatientDetailFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = PatientDetailFragment.class.getSimpleName();
    private static final int PATIENT_LOADER_ID = 0;

    @InjectView(R.id.name_text_view)
    TextView mNameTextView;

    @InjectView(R.id.medical_record_text_view)
    TextView mMedicalRecordTextView;

    private long mPatientId;
    private Patient mPatient;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.d_fragment_patient_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey(PatientAllDetailsFragment.ARG_PATIENT_ID)) {
            mPatientId = getArguments().getLong(PatientAllDetailsFragment.ARG_PATIENT_ID);
            loadPatient();
        }
    }

    private void loadPatient() {
        getLoaderManager().initLoader(PATIENT_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case PATIENT_LOADER_ID:
                final Uri patientUri = PatientContract.PatientEntry.buildPatientUri(mPatientId);
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
        final Patient patient = PatientContract.PatientEntry.readPatient(cursor);
        displayPatient(patient);
    }

    private void displayPatient(final Patient patient) {
        if(patient == null) {
            return;
        }
        mPatient = patient;
        mNameTextView.setText(mPatient.getFirstName() + " " + mPatient.getLastName());
        mMedicalRecordTextView.setText(mPatient.getRecordNumber());
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // no-op
    }
}
