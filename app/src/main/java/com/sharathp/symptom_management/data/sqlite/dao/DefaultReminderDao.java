package com.sharathp.symptom_management.data.sqlite.dao;

import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.data.provider.contract.ReminderContract;
import com.sharathp.symptom_management.model.Reminder;

import javax.inject.Inject;

public class DefaultReminderDao extends DefaultDao<Reminder> implements ReminderDao {

    @Inject
    public DefaultReminderDao() {
        mTable = ReminderContract.ReminderEntry.TABLE_NAME;
    }
}
