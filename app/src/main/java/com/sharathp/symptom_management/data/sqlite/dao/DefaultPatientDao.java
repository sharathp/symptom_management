package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.model.Patient;

import javax.inject.Inject;

public class DefaultPatientDao extends DefaultDao<Patient> implements PatientDao {

    @Inject
    public DefaultPatientDao() {
        mTable = PatientContract.PatientEntry.TABLE_NAME;
    }

    @Override
    public Cursor getPatientsForDoctor(final long doctor_id,
                                       final String[] projection, final String sortOrder) {
        return mDatabase.query(
                mTable,
                projection,
                PatientContract.PatientEntry.COLUMN_DOCTOR_ID + " = ?",
                new String[] {Long.toString(doctor_id)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor getMedicationsForPatient(final long patient_id, final String[] projection, final String sortOrder) {
        // TODO - implement
        return null;
    }

    @Override
    public long addMedicationForPatient(final long patient_id, final long medication_id) {
        final ContentValues values = PatientContract.PatientMedicationEntry.getContentValues(patient_id, medication_id);
        return mDatabase.insert(mTable, null, values);
    }

    @Override
    public int deleteMedicationForPatient(final long patient_id, final long medication_id) {
        return mDatabase.delete(PatientContract.PatientMedicationEntry.TABLE_NAME,
                PatientContract.PatientMedicationEntry.COLUMN_PATIENT_ID + " = ? AND " + PatientContract.PatientMedicationEntry.COLUMN_MEDICATION_ID + " = ?",
                new String[] {Long.toString(patient_id), Long.toString(medication_id)});
    }
}
