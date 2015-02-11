package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.util.MiscUtil.fullyQualify;

public interface CheckinMedicationTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "checkin_medication";

    // Column names
    String COLUMN_MEDICATION_TIME = "medication_time";
    String COLUMN_CHECKIN_ID = "checkin_id";
    String COLUMN_MEDICATION_ID = "medication_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);
    String QUALIFIED_COLUMN_CHECKIN_ID = fullyQualify(TABLE_NAME, COLUMN_CHECKIN_ID);
    String QUALIFIED_COLUMN_MEDICATION_TIME = fullyQualify(TABLE_NAME, COLUMN_MEDICATION_TIME);
    String QUALIFIED_COLUMN_MEDICATION_ID = fullyQualify(TABLE_NAME, COLUMN_MEDICATION_ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MEDICATION_TIME + " INTEGER not null, "
            + COLUMN_CHECKIN_ID + " INTEGER not null, "
            + COLUMN_MEDICATION_ID + " INTEGER not null"
            + ");";
}