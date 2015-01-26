package com.sharathp.symptom_management.model;

import java.util.Date;

/**
 *
 */
public class Reminder {
    private long _id;
    private String id;
    private Date reminderTime;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }
}
