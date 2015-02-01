package com.sharathp.symptom_management.model;


import java.util.List;

public class Doctor {
    private long mId;
    private String mServerId;
    private String mDoctorCode;
    private String mFirstName;
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
