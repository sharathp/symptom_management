package com.sharathp.symptom_management.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MedicationIntake {
    private long id;

    @SerializedName("medication")
    private Medication mMedication;

    @SerializedName("time")
    private Date mTime;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Medication getMedication() {
        return mMedication;
    }

    public void setMedication(Medication medication) {
        this.mMedication = medication;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        this.mTime = time;
    }
}