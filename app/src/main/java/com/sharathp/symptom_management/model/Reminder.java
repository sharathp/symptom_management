package com.sharathp.symptom_management.model;

import java.util.Date;

/**
 *
 */
public class Reminder {
    private long mId;
    private String mServerId;
    private Date mReminderTime;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        this.mServerId = serverId;
    }

    public Date getReminderTime() {
        return mReminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.mReminderTime = reminderTime;
    }
}
