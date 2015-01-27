package com.sharathp.symptom_management.dao.impl;

import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.data.ReminderContract;
import com.sharathp.symptom_management.model.Reminder;

import javax.inject.Inject;

public class DefaultReminderDao extends DefaultDao<Reminder> implements ReminderDao {

    @Inject
    public DefaultReminderDao(final SQLiteDatabase database) {
        mDatabase = database;
        mTable = ReminderContract.ReminderEntry.TABLE_NAME;
    }
}
