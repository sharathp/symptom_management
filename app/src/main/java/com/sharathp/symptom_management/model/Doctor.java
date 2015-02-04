package com.sharathp.symptom_management.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctor {
    private long mId;

    @SerializedName("id")
    private String mServerId;

    @SerializedName("doctorId")
    private String mDoctorCode;

    @SerializedName("firstName")
    private String mFirstName;

    @SerializedName("lastName")
    private String mLastName;
    private List<Patient> mPatients;

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

    public String getDoctorCode() {
        return mDoctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.mDoctorCode = doctorCode;
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

    public List<Patient> getPatients() {
        return mPatients;
    }

    public void setPatients(List<Patient> patients) {
        this.mPatients = patients;
    }
}
