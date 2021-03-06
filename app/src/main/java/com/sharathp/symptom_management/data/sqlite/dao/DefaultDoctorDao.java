package com.sharathp.symptom_management.data.sqlite.dao;

import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.data.sqlite.table.DoctorTable;
import com.sharathp.symptom_management.model.Doctor;

import javax.inject.Inject;

public class DefaultDoctorDao extends DefaultDao<Doctor> implements DoctorDao {

    @Inject
    public DefaultDoctorDao() {
        mTable = DoctorTable.TABLE_NAME;
    }
}
