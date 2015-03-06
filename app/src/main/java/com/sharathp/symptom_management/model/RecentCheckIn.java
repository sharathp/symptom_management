package com.sharathp.symptom_management.model;

/**
 * Subclass of PatientCheckIn that includes the patient information.
 */
public class RecentCheckIn extends PatientCheckIn {
    private Patient mPatient;

    public Patient getPatient() {
        return mPatient;
    }

    public void setPatient(final Patient patient) {
        mPatient = patient;
    }
}