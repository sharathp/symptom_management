package com.sharathp.symptom_management.dao;

import com.sharathp.symptom_management.model.Reminder;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface ReminderDao {
    List<Reminder> getAllReminders();

    Reminder getNextReminder();

    Reminder createReminder(Date dateTime);

    void deleteReminder(Reminder reminder);
}
