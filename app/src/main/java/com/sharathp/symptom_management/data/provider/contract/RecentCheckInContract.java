package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Patient;
import com.sharathp.symptom_management.model.RecentCheckIn;

public class RecentCheckInContract extends SymptomManagementContract  {
    public static final String PATH_RECENT_CHECKINS = "recent-checkins";

    public static final String NUM_RECENT_CHECKINS_PARAM = "numCount";

    public static final class RecentCheckInEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RECENT_CHECKINS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_RECENT_CHECKINS;

        // Column names
        public static final String COLUMN_SERVER_ID = PatientCheckInContract.PatientCheckInEntry.COLUMN_SERVER_ID;
        public static final String COLUMN_CHECKIN_TIME = PatientCheckInContract.PatientCheckInEntry.COLUMN_CHECKIN_TIME;
        public static final String COLUMN_PAIN = PatientCheckInContract.PatientCheckInEntry.COLUMN_PAIN;
        public static final String COLUMN_EATING = PatientCheckInContract.PatientCheckInEntry.COLUMN_EATING;
        public static final String COLUMN_MEDICATED = PatientCheckInContract.PatientCheckInEntry.COLUMN_MEDICATED;
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_PATIENT_FIRST_NAME = "patient_first_name";
        public static final String COLUMN_PATIENT_LAST_NAME = "patient_last_name";

        public static final int COLUMN_PATIENT_ID_INDEX = 6;
        public static final int COLUMN_PATIENT_FIRST_NAME_INDEX = 7;
        public static final int COLUMN_PATIENT_LAST_NAME_INDEX = 8;

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_SERVER_ID,
                COLUMN_CHECKIN_TIME,
                COLUMN_PAIN,
                COLUMN_EATING,
                COLUMN_MEDICATED,
                COLUMN_PATIENT_ID,
                COLUMN_PATIENT_FIRST_NAME,
                COLUMN_PATIENT_LAST_NAME
        };

        public static RecentCheckIn readRecentCheckIn(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }
            final RecentCheckIn recentCheckIn = new RecentCheckIn();
            final Patient patient = new Patient();
            recentCheckIn.setPatient(patient);

            // checkin properties
            PatientCheckInContract.PatientCheckInEntry.populateCheckIn(cursor, recentCheckIn);

            // patient properties
            final long patientId = cursor.getLong(COLUMN_PATIENT_ID_INDEX);
            final String patientFirstName = cursor.getString(COLUMN_PATIENT_FIRST_NAME_INDEX);
            final String patientLastName = cursor.getString(COLUMN_PATIENT_LAST_NAME_INDEX);

            patient.setId(patientId);
            patient.setFirstName(patientFirstName);
            patient.setLastName(patientLastName);
            return recentCheckIn;
        }

        public static ContentValues getContentValues(final RecentCheckIn recentCheckIn) {
            final ContentValues contentValues = PatientCheckInContract.PatientCheckInEntry.getContentValues(recentCheckIn);
            contentValues.put(COLUMN_PATIENT_ID, recentCheckIn.getPatient().getId());
            contentValues.put(COLUMN_PATIENT_FIRST_NAME, recentCheckIn.getPatient().getFirstName());
            contentValues.put(COLUMN_PATIENT_LAST_NAME, recentCheckIn.getPatient().getLastName());
            return contentValues;
        }

        public static Uri buildRecentCheckInsUri(final long doctorId, final int numCheckins) {
            final Uri doctorUri = DoctorContract.DoctorEntry.buildDoctorUri(doctorId);
            return DoctorContract.DoctorEntry.buildDoctorUri(doctorId)
                    .withAppendedPath(doctorUri, PATH_RECENT_CHECKINS)
                    .buildUpon()
                    .appendQueryParameter(NUM_RECENT_CHECKINS_PARAM, String.valueOf(numCheckins))
                    .build();
        }
    }
}