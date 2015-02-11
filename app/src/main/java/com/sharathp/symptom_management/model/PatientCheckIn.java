package com.sharathp.symptom_management.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class PatientCheckIn {
    private long mId;

    @SerializedName("id")
    private String mServerId;

    @SerializedName("checkInTime")
    private Date mCheckinTime;

    @SerializedName("pain")
    private Pain mPain;

    @SerializedName("eating")
    private Eating mEating;

    private boolean mMedicated;

    @SerializedName("medicationIntakes")
    private List<MedicationIntake> mMedicationIntakes;

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

    public List<MedicationIntake> getMedicationIntakes() {
        return mMedicationIntakes;
    }

    public void setMedicationIntakes(List<MedicationIntake> medicationIntakes) {
        this.mMedicationIntakes = medicationIntakes;
    }
}
