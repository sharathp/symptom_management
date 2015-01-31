package com.sharathp.symptom_management.data.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Medication;

public class MedicationContract extends SymptomManagementContract {
    public static final String PATH_MEDICATION = "medications";

    public static Medication readMedication(final Cursor cursor) {
        if(cursor == null || cursor.isAfterLast()) {
            return null;
        }

        final long _id = cursor.getLong(MedicationEntry._ID_INDEX);
        final String id = cursor.getString(MedicationEntry.ID_INDEX);
        final String name = cursor.getString(MedicationEntry.NAME_INDEX);

        final Medication medication = new Medication();
        medication.set_id(_id);
        medication.setId(id);
        medication.setName(name);

        return medication;
    }

    public static ContentValues getContentValues(final Medication medication) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MedicationEntry.COLUMN_ID, medication.getId());
        contentValues.put(MedicationEntry.COLUMN_NAME, medication.getName());
        return contentValues;
    }

    public static final class MedicationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;

        // Table name
        public static final String TABLE_NAME = "medication";

        // Column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ID = "id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_ID,
                COLUMN_NAME
        };

        public static final int _ID_INDEX = 0;
        public static final int ID_INDEX = 1;
        public static final int NAME_INDEX = 2;

        public static final String SQL_CREATE = "CREATE TABLE "
                + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID + " text not null, "
                + COLUMN_NAME + " text not null"
                + ");";

        public static Uri buildMedicationUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
