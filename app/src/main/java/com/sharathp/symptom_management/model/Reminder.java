package com.sharathp.symptom_management.model;

import java.util.Date;

/**
 *
 */
public class Reminder {
    private long id;
    private Date reminderTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }
}
