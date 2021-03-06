package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.util.MiscUtil.fullyQualify;

public interface DoctorTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "doctor";

    // Column names
    String COLUMN_FIRST_NAME = "first_name";
    String COLUMN_LAST_NAME = "last_name";
    String COLUMN_DOCTOR_CODE = "doctor_code";
    String COLUMN_SERVER_ID = "server_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_FIRST_NAME = fullyQualify(TABLE_NAME, COLUMN_FIRST_NAME);
    String QUALIFIED_COLUMN_LAST_NAME = fullyQualify(TABLE_NAME, COLUMN_LAST_NAME);
    String QUALIFIED_COLUMN_DOCTOR_CODE = fullyQualify(TABLE_NAME, COLUMN_DOCTOR_CODE);
    String QUALIFIED_COLUMN_USER_ID = fullyQualify(TABLE_NAME, COLUMN_SERVER_ID);
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIRST_NAME + " text not null, "
            + COLUMN_LAST_NAME + " text not null, "
            + COLUMN_DOCTOR_CODE + " text not null, "
            + COLUMN_SERVER_ID + " text not null"
            + ");";
}
