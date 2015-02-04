package com.sharathp.symptom_management.dao;

import android.database.Cursor;

import com.sharathp.symptom_management.model.Patient;

public interface PatientDao extends Dao<Patient> {

    Cursor getPatientsForDoctor(long doctor_id, String[] projection, String sortOrder);

    Cursor getMedicationsForPatient(long patient_id, String[] projection, String sortOrder);

    long addMedicationForPatient(long patient_id, long medication_id);

    int deleteMedicationForPatient(long patient_id, long medication_id);
}
