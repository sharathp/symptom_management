package com.sharathp.symptom_management.dao.impl;

import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.data.MedicationContract;
import com.sharathp.symptom_management.model.Medication;

import javax.inject.Inject;

public class DefaultMedicationDao extends DefaultDao<Medication> implements MedicationDao {

    @Inject
    public DefaultMedicationDao(final SQLiteDatabase database) {
        mDatabase = database;
        mTable = MedicationContract.MedicationEntry.TABLE_NAME;
    }
}
