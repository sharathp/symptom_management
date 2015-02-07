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
import com.sharathp.symptom_management.data.provider.contract.MedicationContract;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.model.Medication;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class MedicationService extends IntentService {
    private static final String TAG = DoctorService.class.getSimpleName();

    @Inject
    Lazy<SymptomManagementAPI> symptomManagementAPI;

    public static final String ACTION_EXTRA = "action";
    public static final String PATIENT_SERVER_ID_EXTRA = "patientServerId";

    public static final int GET_PATIENT_MEDICATIONS_ACTION = 1;

    public static Intent createGetPatientMedicationsIntent(final Context context, final String patientServerId) {
        final Intent intent = new Intent(context, MedicationService.class);
        intent.putExtra(ACTION_EXTRA, GET_PATIENT_MEDICATIONS_ACTION);
        intent.putExtra(PATIENT_SERVER_ID_EXTRA, patientServerId);
        return intent;
    }

    public MedicationService() {
        super("Medication Service");
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
            case GET_PATIENT_MEDICATIONS_ACTION: {
                final String patientServerId = intent.getStringExtra(PATIENT_SERVER_ID_EXTRA);
                loadPatientMedications(patientServerId);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void loadPatientMedications(final String patientServerId) {
        final long patientId = getPatientId(patientServerId);
        if(patientId == -1L) {
            Log.e(TAG, "Patient not found: " + patientServerId);
            return;
        }
        final List<Medication> medications = symptomManagementAPI.get().getPatientMedications(patientServerId);
        if(medications == null || medications.isEmpty()) {
            Log.d(TAG, "No medications found for patient: " + patientServerId);
            return;
        }
        for(final Medication medication: medications) {
            long existingMedicationId = getMedicationId(medication);
            if(existingMedicationId == -1L) {
                Log.e(TAG, "Medication not found: " + medication.getServerId());
                return;
            }
            associatePatientMedication(patientId, existingMedicationId);
        }
    }

    private void associatePatientMedication(final long patientId, final long medicationId) {
        final Uri uri = PatientContract.PatientMedicationEntry.buildPatientMedicationUri(patientId, medicationId);
        this.getContentResolver().insert(uri, null);
    }

    private long getMedicationId(final Medication medication) {
        final long existingMedication_id = getExistingMedicationId(medication.getServerId());
        if(existingMedication_id != -1L) {
            return existingMedication_id;
        }
        return createNewMedication(medication);
    }

    private long getExistingMedicationId(final String medicationId) {
        final Cursor cursor = this.getContentResolver().query(
                MedicationContract.MedicationEntry.CONTENT_URI,
                new String[]{MedicationContract.MedicationEntry._ID},
                MedicationContract.MedicationEntry.COLUMN_SERVER_ID + " = ?",
                new String[]{medicationId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private long getPatientId(final String serverId) {
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

    private long createNewMedication(final Medication medication) {
        final ContentValues contentValues = MedicationContract.MedicationEntry.getContentValues(medication);
        final Uri insertedUri = this.getContentResolver()
                .insert(MedicationContract.MedicationEntry.CONTENT_URI, contentValues);
        return ContentUris.parseId(insertedUri);
    }
}