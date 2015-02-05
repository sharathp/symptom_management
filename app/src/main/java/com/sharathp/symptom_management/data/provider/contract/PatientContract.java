package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Medication;
import com.sharathp.symptom_management.model.Patient;

public class PatientContract extends SymptomManagementContract {
    public static final String PATH_PATIENT = "patients";

    public static final class PatientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATIENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;

        // Column names
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_RECORD_NUMBER = "record_number";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_RECORD_NUMBER,
                COLUMN_SERVER_ID};

        public static final int _ID_INDEX = 0;
        public static final int COLUMN_FIRST_NAME_INDEX = 1;
        public static final int COLUMN_LAST_NAME_INDEX = 2;
        public static final int COLUMN_RECORD_NUMBER_INDEX = 3;
        public static final int COLUMN_SERVER_ID_INDEX = 4;

        public static Patient readPatient(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }

            final long id = cursor.getLong(_ID_INDEX);
            final String firstName = cursor.getString(COLUMN_FIRST_NAME_INDEX);
            final String lastName = cursor.getString(COLUMN_LAST_NAME_INDEX);
            final String recordNumber = cursor.getString(COLUMN_RECORD_NUMBER_INDEX);
            final String serverId = cursor.getString(COLUMN_SERVER_ID_INDEX);

            final Patient patient = new Patient();
            patient.setId(id);
            patient.setFirstName(firstName);
            patient.setLastName(lastName);
            patient.setRecordNumber(recordNumber);
            patient.setServerId(serverId);
            return patient;
        }

        public static ContentValues getContentValues(final Patient patient) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SERVER_ID, patient.getServerId());
            contentValues.put(COLUMN_FIRST_NAME, patient.getFirstName());
            contentValues.put(COLUMN_LAST_NAME, patient.getLastName());
            contentValues.put(COLUMN_RECORD_NUMBER, patient.getRecordNumber());
            return contentValues;
        }

        public static Uri buildPatientUri(final long patientId) {
            return ContentUris.withAppendedId(CONTENT_URI, patientId);
        }
    }

    public static final class PatientMedicationEntry {

        // Column names
        public static final String COLUMN_MEDICATION_ID = "_id";
        public static final String COLUMN_NAME = "medication_name";
        public static final String COLUMN_SERVER_ID = "medication_server_id";
        public static final String COLUMN_PATIENT_ID = "patient_id";

        public static final int COLUMN_MEDICATION_ID_INDEX = 0;
        public static final int COLUMN_NAME_INDEX = 1;
        public static final int COLUMN_SERVER_ID_INDEX = 2;
        public static final int COLUMN_PATIENT_ID_INDEX = 3;

        public static final String[] ALL_COLUMNS = new String[]{
                COLUMN_MEDICATION_ID,
                COLUMN_NAME,
                COLUMN_SERVER_ID
        };

        public static Medication readMedication(final Cursor cursor) {
            final long id = cursor.getLong(COLUMN_MEDICATION_ID_INDEX);
            final String serverId = cursor.getString(COLUMN_SERVER_ID_INDEX);
            final String name = cursor.getString(COLUMN_NAME_INDEX);

            final Medication medication = new Medication();
            medication.setId(id);
            medication.setServerId(serverId);
            medication.setName(name);

            return medication;
        }

        public static Uri buildPatientMedicationsUri(final long patientId) {
            return Uri.withAppendedPath(PatientEntry.buildPatientUri(patientId), MedicationContract.PATH_MEDICATION);
        }
    }
}
