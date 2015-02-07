package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.util.MiscUtil.fullyQualify;

public interface PatientCheckInTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "patient_checkin";

    // Column names
    String COLUMN_PATIENT_ID = "patient_id";
    String COLUMN_CHECKIN_TIME = "checkin_time";
    String COLUMN_SERVER_ID = "server_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);
    String QUALIFIED_COLUMN_PATIENT_ID = fullyQualify(TABLE_NAME, COLUMN_PATIENT_ID);
    String QUALIFIED_COLUMN_CHECKIN_TIME = fullyQualify(TABLE_NAME, COLUMN_CHECKIN_TIME);
    String QUALIFIED_COLUMN_SERVER_ID = fullyQualify(TABLE_NAME, COLUMN_SERVER_ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CHECKIN_TIME + " INTEGER not null, "
            + COLUMN_SERVER_ID + " text not null, "
            + COLUMN_PATIENT_ID + " INTEGER not null"
            + ");";
}