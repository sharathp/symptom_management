package com.sharathp.symptom_management.model;

import java.util.Date;

/**
 *
 */
public class MedicationIntake {
    private Medication mMedication;
    private Date mTime;

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
