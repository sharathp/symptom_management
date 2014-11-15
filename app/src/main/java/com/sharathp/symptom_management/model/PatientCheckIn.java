package com.sharathp.symptom_management.model;

import java.util.Date;
import java.util.List;

public class PatientCheckIn {
    private String id;
    private Patient patient;
    private Date checkinTime;
    private Pain pain;
    private Eating eating;
    private boolean medicated;
    private List<MedicationIntake> medications;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Pain getPain() {
        return pain;
    }

    public void setPain(Pain pain) {
        this.pain = pain;
    }

    public Eating getEating() {
        return eating;
    }

    public void setEating(Eating eating) {
        this.eating = eating;
    }

    public boolean isMedicated() {
        return medicated;
    }

    public void setMedicated(boolean medicated) {
        this.medicated = medicated;
    }

    public List<MedicationIntake> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationIntake> medications) {
        this.medications = medications;
    }
}
