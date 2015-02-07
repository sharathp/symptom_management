package com.sharathp.symptom_management.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.model.PatientCheckIn;

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

    public static final int GET_PATIENT_MEDICATIONS_ACTION = 1;

    public static Intent createGetPatientCheckInsIntent(final Context context,
                        final String patientServerId, final Date from) {
        final Intent intent = new Intent(context, MedicationService.class);
        intent.putExtra(ACTION_EXTRA, GET_PATIENT_MEDICATIONS_ACTION);
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
            case GET_PATIENT_MEDICATIONS_ACTION: {
                final String patientServerId = intent.getStringExtra(PATIENT_SERVER_ID_EXTRA);
                Date from = (Date) intent.getSerializableExtra(CHECKIN_FROM_EXTRA);
                if(from == null) {
                    from = new Date(0);
                }

                loadPatientMedications(patientServerId, from);
                break;
            }
            default:
                Log.e(TAG, "Unknown action: " + action);
                return;
        }
    }

    private void loadPatientMedications(final String patientServerId, final Date from) {
        final List<PatientCheckIn> checkInList =
                mSymptomManagementAPI.get().getPatientCheckins(patientServerId, from);
    }
}