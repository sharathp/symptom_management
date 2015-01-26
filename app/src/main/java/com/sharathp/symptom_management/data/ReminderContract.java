package com.sharathp.symptom_management.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.Reminder;

public class ReminderContract extends SymptomManagementContract {
    public static final String PATH_REMINDER = "reminder";

    public static Reminder readReminder(final Cursor cursor) {
        if(cursor == null || cursor.isAfterLast()) {
            return null;
        }

        final long _id = cursor.getLong(ReminderEntry._ID_INDEX);
        final String id = cursor.getString(ReminderEntry.ID_INDEX);

        final Reminder reminder = new Reminder();
        reminder.set_id(_id);
        reminder.setId(id);

        return reminder;
    }

    public static ContentValues getContentValues(final Reminder reminder) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderEntry.COLUMN_ID, reminder.getId());
        return contentValues;
    }

    public static final class ReminderEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER;

        // Table name
        public static final String TABLE_NAME = "reminder";

        // Column names
        public static final String COLUMN_ID = "id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_ID
        };

        public static final int _ID_INDEX = 0;
        public static final int ID_INDEX = 1;

        public static final String SQL_CREATE = "CREATE TABLE "
                + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID + " text not null"
                + ");";

        public static Uri buildReminderUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
