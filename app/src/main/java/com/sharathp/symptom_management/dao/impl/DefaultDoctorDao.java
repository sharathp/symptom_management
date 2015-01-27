package com.sharathp.symptom_management.dao.impl;

import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.data.DoctorContract;
import com.sharathp.symptom_management.model.Doctor;

import javax.inject.Inject;

public class DefaultDoctorDao extends DefaultDao<Doctor> implements DoctorDao {

    @Inject
    public DefaultDoctorDao(final SQLiteDatabase database) {
        mDatabase = database;
        mTable = DoctorContract.DoctorEntry.TABLE_NAME;
    }
}
