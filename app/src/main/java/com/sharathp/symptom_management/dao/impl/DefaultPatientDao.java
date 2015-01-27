package com.sharathp.symptom_management.dao.impl;

import android.database.Cursor;

import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.data.PatientContract;
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
}
