package com.sharathp.symptom_management.data.sqlite.table;

import static com.sharathp.symptom_management.task.MiscUtil.fullyQualify;

public interface PatientMedicationTable {

    // Table name
    String TABLE_NAME = "patient_medication";

    // Column names
    String COLUMN_PATIENT_ID = "patient_id";
    String COLUMN_MEDICATION_ID = "medication_id";

    // Qualified Column names
    String QUALIFIED_COLUMN_PATIENT_ID = fullyQualify(TABLE_NAME, COLUMN_PATIENT_ID);
    String QUALIFIED_COLUMN_MEDICATION_ID = fullyQualify(TABLE_NAME, COLUMN_MEDICATION_ID);

    String SQL_CREATE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_PATIENT_ID + " INTEGER not null, "
            + COLUMN_MEDICATION_ID + " INTEGER not null"
            + ");";
}
