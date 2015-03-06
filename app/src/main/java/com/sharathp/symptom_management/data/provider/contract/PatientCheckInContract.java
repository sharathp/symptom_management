package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Eating;
import com.sharathp.symptom_management.model.Medication;
import com.sharathp.symptom_management.model.MedicationIntake;
import com.sharathp.symptom_management.model.Pain;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Date;

public class PatientCheckInContract extends SymptomManagementContract {
    public static final String PATH_CHECKINS = "patient-checkins";

    public static final String PATH_CHECKIN_MEDICATIONS = "checkin-medications";

    public static final class PatientCheckInEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHECKINS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CHECKINS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CHECKINS;

        // Column names
        public static final String COLUMN_CHECKIN_TIME = "checkin_time";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_PAIN = "pain";
        public static final String COLUMN_EATING = "eating";
        public static final String COLUMN_MEDICATED = "medicated";


        public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            COLUMN_SERVER_ID,
            COLUMN_CHECKIN_TIME,
            COLUMN_PAIN,
            COLUMN_EATING,
            COLUMN_MEDICATED
        };

        public static final int _ID_INDEX = 0;
        public static final int COLUMN_SERVER_ID_INDEX = 1;
        public static final int COLUMN_CHECKIN_TIME_INDEX = 2;
        public static final int COLUMN_PAIN_INDEX = 3;
        public static final int COLUMN_EATING_INDEX = 4;
        public static final int COLUMN_MEDICATED_INDEX = 5;

        public static PatientCheckIn readPatientCheckIn(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }

            final PatientCheckIn patientCheckIn = new PatientCheckIn();
            populateCheckIn(cursor, patientCheckIn);
            return patientCheckIn;
        }

        public static ContentValues getContentValues(final PatientCheckIn patientCheckIn) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SERVER_ID, patientCheckIn.getServerId());
            contentValues.put(COLUMN_CHECKIN_TIME, patientCheckIn.getCheckinTime().getTime());
            contentValues.put(COLUMN_PAIN, patientCheckIn.getPain().name());
            contentValues.put(COLUMN_EATING, patientCheckIn.getEating().name());
            contentValues.put(COLUMN_MEDICATED, isMedicated(patientCheckIn) ? 1 : 0);
            return contentValues;
        }

        public static void populateCheckIn(final Cursor cursor, final PatientCheckIn patientCheckIn) {
            final long id = cursor.getLong(_ID_INDEX);
            final String serverId = cursor.getString(COLUMN_SERVER_ID_INDEX);
            final Date checkInTime = new Date(cursor.getLong(COLUMN_CHECKIN_TIME_INDEX));
            final String pain = cursor.getString(COLUMN_PAIN_INDEX);
            final String eating = cursor.getString(COLUMN_EATING_INDEX);
            final int medicated = cursor.getInt(COLUMN_MEDICATED_INDEX);

            patientCheckIn.setId(id);
            patientCheckIn.setCheckinTime(checkInTime);
            patientCheckIn.setServerId(serverId);
            patientCheckIn.setPain(Pain.valueOf(pain));
            patientCheckIn.setEating(Eating.valueOf(eating));
            patientCheckIn.setMedicated(medicated != 0);
        }

        private static boolean isMedicated(final PatientCheckIn patientCheckIn) {
            return patientCheckIn.getMedicationIntakes() != null &&
                    patientCheckIn.getMedicationIntakes().size() > 0;
        }

        public static Uri buildPatientPatientCheckInsUri(final long patientId) {
            final Uri patientUri = PatientContract.PatientEntry.buildPatientUri(patientId);
            return Uri.withAppendedPath(patientUri, PATH_CHECKINS);
        }

        public static Uri buildPatientCheckInUri(final long patientCheckinId) {
            return ContentUris.withAppendedId(CONTENT_URI, patientCheckinId);
        }
    }

    public static final class PatientCheckInMedicationIntakeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHECKIN_MEDICATIONS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CHECKIN_MEDICATIONS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CHECKIN_MEDICATIONS;

        // Column names
        public static final String COLUMN_MEDICATION_TIME = "medication_time";
        public static final String COLUMN_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_MEDICATION_ID = "medication_id";
        public static final String COLUMN_MEDICATION_SERVER_ID = "medication_server_id";
        public static final String COLUMN_CHECKIN_ID = "checkin_id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_MEDICATION_ID,
                COLUMN_MEDICATION_NAME,
                COLUMN_MEDICATION_TIME,
                COLUMN_MEDICATION_SERVER_ID
        };

        public static final int _ID_INDEX = 0;
        public static final int COLUMN_MEDICATION_TIME_INDEX = 1;
        public static final int COLUMN_MEDICATION_NAME_INDEX = 2;
        public static final int COLUMN_MEDICATION_ID_INDEX = 3;
        public static final int COLUMN_MEDICATION_SERVER_ID_INDEX = 4;

        public static MedicationIntake readMedicationIntake(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }

            final long id = cursor.getLong(_ID_INDEX);
            final Date medicationTime = new Date(cursor.getLong(COLUMN_MEDICATION_TIME_INDEX));
            final String medicationName = cursor.getString(COLUMN_MEDICATION_NAME_INDEX);
            final long medicationId = cursor.getLong(COLUMN_MEDICATION_ID_INDEX);
            final String medicationServerId = cursor.getString(COLUMN_MEDICATION_SERVER_ID_INDEX);

            final Medication medication = new Medication();
            medication.setId(medicationId);
            medication.setName(medicationName);
            medication.setServerId(medicationServerId);

            final MedicationIntake medicationIntake = new MedicationIntake();
            medicationIntake.setId(id);
            medicationIntake.setMedication(medication);
            medicationIntake.setTime(medicationTime);

            return medicationIntake;
        }

        public static ContentValues getContentValues(final MedicationIntake medicationIntake) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MEDICATION_TIME, medicationIntake.getTime().getTime());
            contentValues.put(COLUMN_MEDICATION_ID, medicationIntake.getMedication().getId());
            contentValues.put(COLUMN_MEDICATION_SERVER_ID, medicationIntake.getMedication().getServerId());
            contentValues.put(COLUMN_MEDICATION_NAME, medicationIntake.getMedication().getName());
            return contentValues;
        }

        public static Uri buildCheckInMedicationsUri(final long checkinId) {
            final Uri checkinUri = PatientCheckInEntry.buildPatientCheckInUri(checkinId);
            return Uri.withAppendedPath(checkinUri, PATH_CHECKIN_MEDICATIONS);
        }

        public static Uri buildCheckInMedicationUri(final long checkinMedicationId) {
            return ContentUris.withAppendedId(CONTENT_URI, checkinMedicationId);
        }
    }
}