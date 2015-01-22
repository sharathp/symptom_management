package com.sharathp.symptom_management.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class PatientContract extends SymptomManagementContract {
    public static final String PATH_PATIENT = "patient";


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

        public static final String SQL_CREATE = "CREATE TABLE "
                + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " text not null, "
                + COLUMN_LAST_NAME + " text not null, "
                + COLUMN_PATIENT_ID + " text not null, "
                + COLUMN_USER_ID + " text not null"
                + ");";

        public static Uri buildPatientUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
