package com.sharathp.symptom_management.model;

import java.util.Date;

/**
 *
 */
public class MedicationIntake {
    private Medication medication;
    private Date time;

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
