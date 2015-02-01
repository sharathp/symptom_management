package com.sharathp.symptom_management.model;


import java.util.List;

public class Patient {
    private long mId;
    private String mServerId;
    private String mFirstName;
    private String mLastName;
    private String mRecordNumber;
    private List<Medication> mMedications;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getRecordNumber() {
        return mRecordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.mRecordNumber = recordNumber;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        this.mServerId = serverId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public List<Medication> getMedications() {
        return mMedications;
    }

    public void setMedications(List<Medication> medications) {
        this.mMedications = medications;
    }

    @Override
    public String toString() {
        return mServerId;
    }
}
