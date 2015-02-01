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
import com.sharathp.symptom_management.data.provider.contract.DoctorContract;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.model.Patient;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class PatientService extends IntentService {
    private static final String TAG = PatientService.class.getSimpleName();

    @Inject
    Lazy<SymptomManagementAPI> symptomManagementAPI;

    public static final String ACTION_EXTRA = "action";
    public static final int UPDATE_PATIENTS_ACTION = 1;

    public static final String DOCTOR_USER_ID_EXTRA = "doctorUserId";

    public static Intent createUpdatePatientsIntent(final Context context, final String doctorUserId) {
        final Intent intent = new Intent(context, PatientService.class);
        intent.putExtra(ACTION_EXTRA, UPDATE_PATIENTS_ACTION);
        intent.putExtra(DOCTOR_USER_ID_EXTRA, doctorUserId);
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
                final String doctorUserId = intent.getStringExtra(DOCTOR_USER_ID_EXTRA);
                updatePatients(doctorUserId);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void updatePatients(final String doctorUserId) {
        if(doctorUserId == null) {
            return;
        }
        final long doctor_id = getExistingDoctor_id(doctorUserId);
        if(doctor_id == -1L) {
            return;
        }

        // TODO - remove existing patients who are no longer associated to doctor..
        final List<Patient> patients = symptomManagementAPI.get().getPatientsForDoctor(doctorUserId);
        for (final Patient patient : patients) {
            final long existing_patient_id = getExistingPatient_Id(patient.getServerId());
            if (existing_patient_id == -1L) {
                final long newId = createNewPatient(patient, doctor_id);
                Log.d(TAG, "Created new patient: " + newId);
            } else {
                updateExistingPatient(existing_patient_id, patient, doctor_id);
                Log.d(TAG, "Updated existing patient: " + existing_patient_id);
            }
        }
    }

    private long getExistingPatient_Id(final String userId) {
        final Cursor cursor = this.getContentResolver().query(
                PatientContract.PatientEntry.CONTENT_URI,
                new String[]{PatientContract.PatientEntry._ID},
                PatientContract.PatientEntry.COLUMN_USER_ID + " = ?",
                new String[]{userId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private long getExistingDoctor_id(final String userId) {
        final Cursor cursor = this.getContentResolver().query(
                DoctorContract.DoctorEntry.CONTENT_URI,
                new String[]{DoctorContract.DoctorEntry._ID},
                DoctorContract.DoctorEntry.COLUMN_USER_ID + " = ?",
                new String[]{userId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private void updateExistingPatient(final long existing_Id,
                               final Patient patient, final long doctor_id) {
        final ContentValues contentValues = getContentValues(patient, doctor_id);
        final Uri uri = PatientContract.PatientEntry.buildPatientUri(existing_Id);
        this.getContentResolver().update(uri, contentValues, null, null);
    }

    private long createNewPatient(final Patient patient, final long doctor_id) {
        final ContentValues contentValues = getContentValues(patient, doctor_id);
        final Uri insertedUri = this.getContentResolver()
                .insert(PatientContract.PatientEntry.CONTENT_URI, contentValues);
        return ContentUris.parseId(insertedUri);
    }

    private ContentValues getContentValues(final Patient patient, final long doctor_id) {
        final ContentValues contentValues = PatientContract.getContentValues(patient);
        contentValues.put(PatientContract.PatientEntry.COLUMN_DOCTOR_ID, doctor_id);
        return contentValues;
    }
}