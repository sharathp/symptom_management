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
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.model.PatientCheckIn;
import com.sharathp.symptom_management.model.QueryParamDate;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class PatientCheckInService extends IntentService {
    private static final String TAG = PatientCheckInService.class.getSimpleName();

    @Inject
    Lazy<SymptomManagementAPI> mSymptomManagementAPI;

    public static final String ACTION_EXTRA = "action";
    public static final String PATIENT_SERVER_ID_EXTRA = "patientServerId";
    public static final String CHECKIN_FROM_EXTRA = "from";

    public static final int GET_PATIENT_CHECKINS_ACTION = 1;

    public static Intent createGetPatientCheckInsIntent(final Context context,
                        final String patientServerId, final Date from) {
        final Intent intent = new Intent(context, PatientCheckInService.class);
        intent.putExtra(ACTION_EXTRA, GET_PATIENT_CHECKINS_ACTION);
        intent.putExtra(PATIENT_SERVER_ID_EXTRA, patientServerId);
        intent.putExtra(CHECKIN_FROM_EXTRA, from);
        return intent;
    }

    public PatientCheckInService() {
        super("Patient Checkin Service");
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
            case GET_PATIENT_CHECKINS_ACTION: {
                final String patientServerId = intent.getStringExtra(PATIENT_SERVER_ID_EXTRA);
                Date from = (Date) intent.getSerializableExtra(CHECKIN_FROM_EXTRA);
                if(from == null) {
                    from = new Date(0);
                }

                loadPatientCheckIns(patientServerId, from);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void loadPatientCheckIns(final String patientServerId, final Date from) {
        final long patientId = getPatientId(patientServerId);

        if(patientId == -1L) {
            Log.e(TAG, "Patient not found: " + patientServerId);
        }

        final List<PatientCheckIn> patientCheckins =
                mSymptomManagementAPI.get().getPatientCheckins(patientServerId, new QueryParamDate(from));

        for(final PatientCheckIn patientCheckIn: patientCheckins) {
            final long patientCheckInId = getPatientCheckInId(patientCheckIn.getServerId());
            if(patientCheckInId != -1L) {
                Log.d(TAG, "Patient Check-in exists: " + patientCheckIn.getServerId());
                continue;
            }
            final long newPatientCheckInId = createPatientCheckIn(patientId, patientCheckIn);
            Log.d(TAG, "Created new Patient Check-in : " + newPatientCheckInId);
        }
    }

    private long createPatientCheckIn(final long patientId, final PatientCheckIn patientCheckIn) {
        final Uri patientCheckInUri = PatientCheckInContract.PatientCheckInEntry.buildPatientPatientCheckInsUri(patientId);
        final ContentValues contentValues = PatientCheckInContract.PatientCheckInEntry.getContentValues(patientCheckIn);
        final Uri insertedUri = this.getContentResolver().insert(patientCheckInUri, contentValues);
        return ContentUris.parseId(insertedUri);
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

    private long getPatientCheckInId(final String serverId) {
        final Cursor cursor = this.getContentResolver().query(
                PatientCheckInContract.PatientCheckInEntry.CONTENT_URI,
                new String[]{PatientCheckInContract.PatientCheckInEntry._ID},
                PatientCheckInContract.PatientCheckInEntry.COLUMN_SERVER_ID + " = ?",
                new String[]{serverId}, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1L;
    }
}