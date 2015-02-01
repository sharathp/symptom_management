package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Doctor;

public class DoctorContract extends SymptomManagementContract {
    public static final String PATH_DOCTOR = "doctors";

    public static Doctor readDoctor(final Cursor cursor) {
        if(cursor == null || cursor.isAfterLast()) {
            return null;
        }

        final long _id = cursor.getLong(DoctorEntry._ID_INDEX);
        final String firstName = cursor.getString(DoctorEntry.FIRST_NAME_INDEX);
        final String lastName = cursor.getString(DoctorEntry.LAST_NAME_INDEX);
        final String doctorId = cursor.getString(DoctorEntry.DOCTOR_ID_INDEX);
        final String userId = cursor.getString(DoctorEntry.USER_ID_INDEX);

        final Doctor doctor = new Doctor();
        doctor.set_id(_id);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setDoctorId(doctorId);
        doctor.setId(userId);
        return doctor;
    }

    public static ContentValues getContentValues(final Doctor doctor) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DoctorEntry.COLUMN_USER_ID, doctor.getId());
        contentValues.put(DoctorEntry.COLUMN_FIRST_NAME, doctor.getFirstName());
        contentValues.put(DoctorEntry.COLUMN_LAST_NAME, doctor.getLastName());
        contentValues.put(DoctorEntry.COLUMN_DOCTOR_ID, doctor.getDoctorId());
        return contentValues;
    }

    public static final class DoctorEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOCTOR).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DOCTOR;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DOCTOR;

        // Table name
        public static final String TABLE_NAME = "doctor";

        // Column names
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_USER_ID = "user_id";

        public static final int _ID_INDEX = 0;
        public static final int FIRST_NAME_INDEX = 1;
        public static final int LAST_NAME_INDEX = 2;
        public static final int DOCTOR_ID_INDEX = 3;
        public static final int USER_ID_INDEX = 4;

        public static final String[] ALL_COLUMNS = new String[]{_ID, COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME, COLUMN_DOCTOR_ID, COLUMN_USER_ID};

        public static Uri buildDoctorUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPatientsUri(final long id) {
            final Uri doctorUri = buildDoctorUri(id);
            return Uri.withAppendedPath(doctorUri, PatientContract.PATH_PATIENT);
        }
    }
}