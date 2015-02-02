package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.data.sqlite.table.MedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientTable;
import com.sharathp.symptom_management.model.Patient;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DefaultPatientDao extends DefaultDao<Patient> implements PatientDao {

    private static final String PATIENT_MEDICATION_MEDICATION_JOIN_TABLES =
            MedicationTable.TABLE_NAME + " m INNER JOIN " +
            PatientMedicationTable.TABLE_NAME + " pm ON m." + MedicationTable._ID +
            " = pm." + PatientMedicationTable.COLUMN_MEDICATION_ID;

    private static final Map<String, String> PATIENT_MEDICATION_MEDICATION_PROJECTION
            = new HashMap<String, String>();

    static {
        PATIENT_MEDICATION_MEDICATION_PROJECTION.put(PatientContract.PatientMedicationEntry.COLUMN_MEDICATION_ID,
            "m." + MedicationTable._ID + " AS " + PatientContract.PatientMedicationEntry.COLUMN_MEDICATION_ID);

        PATIENT_MEDICATION_MEDICATION_PROJECTION.put(PatientContract.PatientMedicationEntry.COLUMN_NAME,
                "m." + MedicationTable.COLUMN_NAME + " AS " + PatientContract.PatientMedicationEntry.COLUMN_NAME);

        PATIENT_MEDICATION_MEDICATION_PROJECTION.put(PatientContract.PatientMedicationEntry.COLUMN_SERVER_ID,
                "m." + MedicationTable.COLUMN_SERVER_ID + " AS " + PatientContract.PatientMedicationEntry.COLUMN_SERVER_ID);

        PATIENT_MEDICATION_MEDICATION_PROJECTION.put(PatientContract.PatientMedicationEntry.COLUMN_PATIENT_ID,
                "pm." + PatientMedicationTable.COLUMN_PATIENT_ID + " AS " + PatientContract.PatientMedicationEntry.COLUMN_PATIENT_ID);
    }

    @Inject
    public DefaultPatientDao() {
        mTable = PatientTable.TABLE_NAME;
    }

    @Override
    public Cursor getPatientsForDoctor(final long doctorId,
                                       final String[] projection, final String sortOrder) {
        return mDatabase.query(
                mTable,
                projection,
                PatientTable.COLUMN_DOCTOR_ID + " = ?",
                new String[] {Long.toString(doctorId)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor getMedicationsForPatient(final long patientId, final String[] projection, final String sortOrder) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(PATIENT_MEDICATION_MEDICATION_JOIN_TABLES);
        sqLiteQueryBuilder.setProjectionMap(PATIENT_MEDICATION_MEDICATION_PROJECTION);

        return sqLiteQueryBuilder.query(
                mDatabase,
                projection,
                PatientContract.PatientMedicationEntry.COLUMN_PATIENT_ID + " = ?",
                new String[] {Long.toString(patientId)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public long addMedicationForPatient(final long patientId, final long medicationId) {
        final ContentValues values = new ContentValues();
        values.put(PatientMedicationTable.COLUMN_MEDICATION_ID, medicationId);
        values.put(PatientMedicationTable.COLUMN_PATIENT_ID, patientId);
        return mDatabase.insert(mTable, null, values);
    }

    @Override
    public int deleteMedicationForPatient(final long patientId, final long medicationId) {
        return mDatabase.delete(PatientMedicationTable.TABLE_NAME,
                PatientMedicationTable.COLUMN_PATIENT_ID + " = ? AND " + PatientMedicationTable.COLUMN_MEDICATION_ID + " = ?",
                new String[] {Long.toString(patientId), Long.toString(medicationId)});
    }
}