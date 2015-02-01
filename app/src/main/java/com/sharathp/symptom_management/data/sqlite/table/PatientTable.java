package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.task.MiscUtil.fullyQualify;

public interface PatientTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "patient";

    // Column names
    String COLUMN_FIRST_NAME = "first_name";
    String COLUMN_LAST_NAME = "last_name";
    String COLUMN_PATIENT_ID = "patient_id";
    String COLUMN_USER_ID = "user_id";
    String COLUMN_DOCTOR_ID = "doctor_id";

    // Column names
    String QUALIFIED_COLUMN_FIRST_NAME = fullyQualify(TABLE_NAME, COLUMN_FIRST_NAME);
    String QUALIFIED_COLUMN_LAST_NAME = fullyQualify(TABLE_NAME, COLUMN_LAST_NAME);
    String QUALIFIED_COLUMN_PATIENT_ID = fullyQualify(TABLE_NAME, COLUMN_PATIENT_ID);
    String QUALIFIED_COLUMN_USER_ID = fullyQualify(TABLE_NAME, COLUMN_USER_ID);
    String QUALIFIED_COLUMN_DOCTOR_ID = fullyQualify(TABLE_NAME, COLUMN_DOCTOR_ID);
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIRST_NAME + " text not null, "
            + COLUMN_LAST_NAME + " text not null, "
            + COLUMN_PATIENT_ID + " text not null, "
            + COLUMN_USER_ID + " text not null, "
            + COLUMN_DOCTOR_ID + " text not null"
            + ");";
}