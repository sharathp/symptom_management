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
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.Doctor;

import javax.inject.Inject;

import dagger.Lazy;

public class DoctorService extends IntentService {
    private static final String TAG = DoctorService.class.getSimpleName();

    @Inject
    Lazy<SymptomManagementAPI> symptomManagementAPI;

    public static final String ACTION_EXTRA = "action";
    public static final String DOCTOR_USER_ID_EXTRA = "userId";

    public static final int GET_DOCTOR_ACTION = 1;

    public static Intent createGetDoctorIntent(final Context context, final String userId) {
        final Intent intent = new Intent(context, DoctorService.class);
        intent.putExtra(ACTION_EXTRA, GET_DOCTOR_ACTION);
        intent.putExtra(DOCTOR_USER_ID_EXTRA, userId);
        return intent;
    }

    public DoctorService() {
        super("Doctor Service");
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
                final String userId = intent.getStringExtra(DOCTOR_USER_ID_EXTRA);
                getDoctor(userId);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void getDoctor(final String userId) {
        if(userId == null) {
            return;
        }
        final Doctor doctor = symptomManagementAPI.get().getDoctorById(userId);
        if(doctor == null) {
            return;
        }

        long existing_id = getExisting_id(doctor.getId());
        if (existing_id == -1L) {
            existing_id = createNewDoctor(doctor);
            Log.d(TAG, "Created new doctor: " + existing_id);
            retrievePatients(doctor.getId());
        } else {
            updateExistingDoctor(existing_id, doctor);
            Log.d(TAG, "Updated existing doctor: " + existing_id);
        }
        final Session session = Session.restore(this);
        if(session != null) {
            session.set_id(this, existing_id);
        }
    }

    private long getExisting_id(final String userId) {
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

    private void retrievePatients(final String doctorUserId) {
        // TODO - pass id as argument to patient service
        final Intent intent = PatientService.createUpdatePatientsIntent(this, doctorUserId);
        startService(intent);
    }

    private void updateExistingDoctor(final long existing_doctor_id, final Doctor doctor) {
        final ContentValues contentValues = DoctorContract.getContentValues(doctor);
        final Uri uri = DoctorContract.DoctorEntry.buildDoctorUri(existing_doctor_id);
        this.getContentResolver().update(uri, contentValues, null, null);
    }

    private long createNewDoctor(final Doctor doctor) {
        final ContentValues contentValues = DoctorContract.getContentValues(doctor);
        final Uri insertedUri = this.getContentResolver()
                .insert(DoctorContract.DoctorEntry.CONTENT_URI, contentValues);
        return ContentUris.parseId(insertedUri);
    }
}
