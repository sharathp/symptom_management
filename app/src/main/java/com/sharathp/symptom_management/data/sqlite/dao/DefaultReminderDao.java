package com.sharathp.symptom_management.data.sqlite.dao;

import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.data.sqlite.table.ReminderTable;
import com.sharathp.symptom_management.model.Reminder;

import javax.inject.Inject;

public class DefaultReminderDao extends DefaultDao<Reminder> implements ReminderDao {

    @Inject
    public DefaultReminderDao() {
        mTable = ReminderTable.TABLE_NAME;
    }
}
