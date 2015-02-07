package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.util.MiscUtil.fullyQualify;

public interface ReminderTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "reminder";

    // Column names
    String COLUMN_SERVER_ID = "server_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_SERVER_ID = fullyQualify(TABLE_NAME, COLUMN_SERVER_ID);
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SERVER_ID + " text not null"
            + ");";
}