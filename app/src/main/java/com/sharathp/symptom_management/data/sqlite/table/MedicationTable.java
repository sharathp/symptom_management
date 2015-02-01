package com.sharathp.symptom_management.data.sqlite.table;

import android.provider.BaseColumns;

import static com.sharathp.symptom_management.task.MiscUtil.fullyQualify;

public interface MedicationTable extends BaseColumns {

    // Table name
    String TABLE_NAME = "medication";

    // Column names
    String COLUMN_NAME = "name";
    String COLUMN_MEDICATION_ID = "medication_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_NAME = fullyQualify(TABLE_NAME, COLUMN_NAME);
    String QUALIFIED_COLUMN_MEDICATION_ID = fullyQualify(TABLE_NAME, COLUMN_MEDICATION_ID);
    String QUALIFIED_COLUMN_ID = fullyQualify(TABLE_NAME, _ID);


    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MEDICATION_ID + " text not null, "
            + COLUMN_NAME + " text not null"
            + ");";
}