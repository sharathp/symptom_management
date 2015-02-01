package com.sharathp.symptom_management.data.sqlite.dao;

import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.data.provider.contract.MedicationContract;
import com.sharathp.symptom_management.model.Medication;

import javax.inject.Inject;

public class DefaultMedicationDao extends DefaultDao<Medication> implements MedicationDao {

    @Inject
    public DefaultMedicationDao() {
        mTable = MedicationContract.MedicationEntry.TABLE_NAME;
    }
}
