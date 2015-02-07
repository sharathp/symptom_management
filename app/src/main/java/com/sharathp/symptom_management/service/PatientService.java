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

    public static final String DOCTOR_SERVER_ID_EXTRA = "doctorServerId";

    public static Intent createUpdatePatientsIntent(final Context context, final String doctorServerId) {
        final Intent intent = new Intent(context, PatientService.class);
        intent.putExtra(ACTION_EXTRA, UPDATE_PATIENTS_ACTION);
        intent.putExtra(DOCTOR_SERVER_ID_EXTRA, doctorServerId);
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
            case UPDATE_PATIENTS_ACTION: {
                final String doctorServerId = intent.getStringExtra(DOCTOR_SERVER_ID_EXTRA);
                updatePatients(doctorServerId);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void updatePatients(final String doctorServerId) {
        if(doctorServerId == null) {
            return;
        }
        final long doctorId = getExistingDoctorId(doctorServerId);
        if(doctorId == -1L) {
            return;
        }

        // TODO - remove existing patients who are no longer associated to doctor..
        final List<Patient> patients = symptomManagementAPI.get().getPatientsForDoctor(doctorServerId);
        for (final Patient patient : patients) {
            final long existingPatientId = getExistingPatientId(patient.getServerId());
            if (existingPatientId == -1L) {
                long newPatientId = createNewPatient(patient, doctorId);
                Log.d(TAG, "Created new patient: " + newPatientId);
            } else {
                updateExistingPatient(existingPatientId, patient, doctorId);
                Log.d(TAG, "Updated existing patient: " + existingPatientId);
            }
            loadMedications(patient.getServerId());
            loadPatientCheckIns(patient.getServerId());
        }
    }

    private void loadMedications(final String patientServerId) {
        final Intent intent = MedicationService.createGetPatientMedicationsIntent(this, patientServerId);
        startService(intent);
    }

    private void loadPatientCheckIns(final String patientServerId) {
        final Intent intent = PatientCheckInService.createGetPatientCheckInsIntent(this, patientServerId, null);
        startService(intent);
    }

    private long getExistingPatientId(final String serverId) {
        final Cursor cursor = this.getContentResolver().query(
                PatientContract.PatientEntry.CONTENT_URI,
                new String[]{PatientContract.PatientEntry._ID},
                PatientContract.PatientEntry.COLUMN_SERVER_ID + " = ?",
                new String[]{serverId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private long getExistingDoctorId(final String serverId) {
        final Cursor cursor = this.getContentResolver().query(
                DoctorContract.DoctorEntry.CONTENT_URI,
                new String[]{DoctorContract.DoctorEntry._ID},
                DoctorContract.DoctorEntry.COLUMN_SERVER_ID + " = ?",
                new String[]{serverId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private void updateExistingPatient(final long patientId,
                               final Patient patient, final long doctorId) {
        final ContentValues contentValues = getContentValues(patient, doctorId);
        final Uri uri = PatientContract.PatientEntry.buildPatientUri(patientId);
        this.getContentResolver().update(uri, contentValues, null, null);
    }

    private long createNewPatient(final Patient patient, final long doctorId) {
        final ContentValues contentValues = getContentValues(patient, doctorId);
        final Uri insertedUri = this.getContentResolver()
                .insert(PatientContract.PatientEntry.CONTENT_URI, contentValues);
        return ContentUris.parseId(insertedUri);
    }

    private ContentValues getContentValues(final Patient patient, final long doctorId) {
        final ContentValues contentValues = PatientContract.PatientEntry.getContentValues(patient);
        contentValues.put(PatientContract.PatientEntry.COLUMN_DOCTOR_ID, doctorId);
        return contentValues;
    }
}