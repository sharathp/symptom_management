package com.sharathp.symptom_management.data.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Patient;

public class PatientContract extends SymptomManagementContract {
    public static final String PATH_PATIENT = "patient";

    public static Patient readPatient(final Cursor cursor) {
        if(cursor == null || cursor.isAfterLast()) {
            return null;
        }

        final long _id = cursor.getLong(PatientEntry._ID_INDEX);
        final String firstName = cursor.getString(PatientEntry.FIRST_NAME_INDEX);
        final String lastName = cursor.getString(PatientEntry.LAST_NAME_INDEX);
        final String patientId = cursor.getString(PatientEntry.PATIENT_ID_INDEX);
        final String userId = cursor.getString(PatientEntry.USER_ID_INDEX);

        final Patient patient = new Patient();
        patient.set_id(_id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPatientId(patientId);
        patient.setId(userId);
        return patient;
    }

    public static ContentValues getContentValues(final Patient patient) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(PatientEntry.COLUMN_USER_ID, patient.getId());
        contentValues.put(PatientEntry.COLUMN_FIRST_NAME, patient.getFirstName());
        contentValues.put(PatientEntry.COLUMN_LAST_NAME, patient.getLastName());
        contentValues.put(PatientEntry.COLUMN_PATIENT_ID, patient.getPatientId());
        return contentValues;
    }

    public static final class PatientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATIENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;

        // Table name
        public static final String TABLE_NAME = "patient";

        // Column names
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PATIENT_ID,
                COLUMN_USER_ID};

        public static final int _ID_INDEX = 0;
        public static final int FIRST_NAME_INDEX = 1;
        public static final int LAST_NAME_INDEX = 2;
        public static final int PATIENT_ID_INDEX = 3;
        public static final int USER_ID_INDEX = 4;

        public static final String SQL_CREATE = "CREATE TABLE "
                + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " text not null, "
                + COLUMN_LAST_NAME + " text not null, "
                + COLUMN_PATIENT_ID + " text not null, "
                + COLUMN_USER_ID + " text not null, "
                + COLUMN_DOCTOR_ID + " text not null"
                + ");";

        public static Uri buildPatientUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
