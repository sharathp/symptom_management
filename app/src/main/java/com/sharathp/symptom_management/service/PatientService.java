package com.sharathp.symptom_management.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.data.PatientContract;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.Patient;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class PatientService extends IntentService {
    private static final String TAG = PatientService.class.getSimpleName();

    @Inject
    Lazy<SymptomManagementAPI> symptomManagementAPI;

    public static final String ACTION_EXTRA = "action";
    public static final int UPDATE_PATIENTS_ACTION = 1;

    public static Intent createUpdatePatientsIntent(final Context context) {
        final Intent intent = new Intent(context, PatientService.class);
        intent.putExtra(ACTION_EXTRA, UPDATE_PATIENTS_ACTION);
        return intent;
    }

    public PatientService() {
        super("Patient Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final SymptomManagementApplication application = (SymptomManagementApplication) getApplication();
        application.inject(this);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final int action = intent.getIntExtra(ACTION_EXTRA, -1);
        switch (action) {
            case 1: {
                updatePatients();
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void updatePatients() {
        final Session session = Session.restore(getApplicationContext());
        if (session.isPatient() || session.getUserId() == null) {
            return;
        }
        final List<Patient> patients = symptomManagementAPI.get().getPatientsForDoctor(session.getUserId());
        for (final Patient patient : patients) {
            final long existing_Id = getExisting_Id(patient.getId());
            if (existing_Id == -1L) {
                final long newId = createNewPatient(patient);
                Log.d(TAG, "Created new patient: " + newId);
            } else {
                updateExistingPatient(existing_Id, patient);
                Log.d(TAG, "Updated existing patient: " + existing_Id);
            }
        }
    }

    private long getExisting_Id(final String userId) {
        final Cursor patientCursor = this.getContentResolver().query(
                PatientContract.PatientEntry.CONTENT_URI,
                new String[]{PatientContract.PatientEntry._ID},
                PatientContract.PatientEntry.COLUMN_USER_ID + " = ?",
                new String[]{userId}, null);

        if (patientCursor.moveToFirst()) {
            final int _idIndex = patientCursor.getColumnIndex(PatientContract.PatientEntry._ID);
            return patientCursor.getLong(_idIndex);
        }
        return -1L;
    }


    private void updateExistingPatient(final long existing_Id, final Patient patient) {
        final ContentValues contentValues = getContentValues(patient);
        final Uri patientUri = PatientContract.PatientEntry.buildPatientUri(existing_Id);
        this.getContentResolver().update(patientUri, contentValues, null, null);
    }

    private long createNewPatient(final Patient patient) {
        final ContentValues contentValues = getContentValues(patient);
        final Uri insertedUri = this.getContentResolver()
                .insert(PatientContract.PatientEntry.CONTENT_URI, contentValues);
        return ContentUris.parseId(insertedUri);
    }

    private ContentValues getContentValues(final Patient patient) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(PatientContract.PatientEntry.COLUMN_USER_ID, patient.getId());
        contentValues.put(PatientContract.PatientEntry.COLUMN_FIRST_NAME, patient.getFirstName());
        contentValues.put(PatientContract.PatientEntry.COLUMN_LAST_NAME, patient.getLastName());
        contentValues.put(PatientContract.PatientEntry.COLUMN_PATIENT_ID, patient.getPatientId());
        return contentValues;
    }
}
