package com.sharathp.symptom_management.dao.impl;

import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.data.contract.DoctorContract;
import com.sharathp.symptom_management.model.Doctor;

import javax.inject.Inject;

public class DefaultDoctorDao extends DefaultDao<Doctor> implements DoctorDao {

    @Inject
    public DefaultDoctorDao() {
        mTable = DoctorContract.DoctorEntry.TABLE_NAME;
    }
}
