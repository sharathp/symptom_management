package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.data.sqlite.table.PatientMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientTable;
import com.sharathp.symptom_management.model.Patient;

import javax.inject.Inject;

public class DefaultPatientDao extends DefaultDao<Patient> implements PatientDao {

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
        // TODO - implement
        return null;
    }

    @Override
    public long addMedicationForPatient(final long patientId, final long medicationId) {
        final ContentValues values = null;
        return mDatabase.insert(mTable, null, values);
    }

    @Override
    public int deleteMedicationForPatient(final long patientId, final long medicationId) {
        return mDatabase.delete(PatientMedicationTable.TABLE_NAME,
                PatientMedicationTable.COLUMN_PATIENT_ID + " = ? AND " + PatientMedicationTable.COLUMN_MEDICATION_ID + " = ?",
                new String[] {Long.toString(patientId), Long.toString(medicationId)});
    }
}
