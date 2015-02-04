package com.sharathp.symptom_management.model;

import java.util.Date;
import java.util.List;

public class PatientCheckIn {
    private long mId;
    private String mServerId;
    private Patient mPatient;
    private Date mCheckinTime;
    private Pain mPain;
    private Eating mEating;
    private boolean mMedicated;
    private List<MedicationIntake> mMedications;

    public long getId() {
        return mId;
    }

    public void setId(final long id) {
        mId = id;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        this.mServerId = serverId;
    }

    public Patient getPatient() {
        return mPatient;
    }

    public void setPatient(Patient patient) {
        this.mPatient = patient;
    }

    public Date getCheckinTime() {
        return mCheckinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.mCheckinTime = checkinTime;
    }

    public Pain getPain() {
        return mPain;
    }

    public void setPain(Pain pain) {
        this.mPain = pain;
    }

    public Eating getEating() {
        return mEating;
    }

    public void setEating(Eating eating) {
        this.mEating = eating;
    }

    public boolean isMedicated() {
        return mMedicated;
    }

    public void setMedicated(boolean medicated) {
        this.mMedicated = medicated;
    }

    public List<MedicationIntake> getMedications() {
        return mMedications;
    }

    public void setMedications(List<MedicationIntake> medications) {
        this.mMedications = medications;
    }
}
