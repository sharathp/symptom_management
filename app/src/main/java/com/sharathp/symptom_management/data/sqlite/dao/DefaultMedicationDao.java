package com.sharathp.symptom_management.data.sqlite.dao;

import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.data.sqlite.table.MedicationTable;
import com.sharathp.symptom_management.model.Medication;

import javax.inject.Inject;

public class DefaultMedicationDao extends DefaultDao<Medication> implements MedicationDao {

    @Inject
    public DefaultMedicationDao() {
        mTable = MedicationTable.TABLE_NAME;
    }
}
