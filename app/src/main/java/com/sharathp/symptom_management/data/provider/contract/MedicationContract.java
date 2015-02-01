package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Medication;

public class MedicationContract extends SymptomManagementContract {
    public static final String PATH_MEDICATION = "medications";

    public static final class MedicationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;

        // Column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVER_ID = "server_id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_SERVER_ID,
                COLUMN_NAME
        };

        public static final int _ID_INDEX = 0;
        public static final int COLUMN_SERVER_ID_INDEX = 1;
        public static final int COLUMN_NAME_INDEX = 2;

        public static Medication readMedication(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }

            final long id = cursor.getLong(_ID_INDEX);
            final String serverId = cursor.getString(COLUMN_SERVER_ID_INDEX);
            final String name = cursor.getString(COLUMN_NAME_INDEX);

            final Medication medication = new Medication();
            medication.setId(id);
            medication.setServerId(serverId);
            medication.setName(name);

            return medication;
        }

        public static ContentValues getContentValues(final Medication medication) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SERVER_ID, medication.getServerId());
            contentValues.put(COLUMN_NAME, medication.getName());
            return contentValues;
        }

        public static Uri buildMedicationUri(final long medicationId) {
            return ContentUris.withAppendedId(CONTENT_URI, medicationId);
        }
    }
}