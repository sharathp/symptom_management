package com.sharathp.symptom_management.dao;

import android.database.Cursor;

import com.sharathp.symptom_management.model.Patient;

public interface PatientDao extends Dao<Patient> {

    Cursor getPatientsForDoctor(long doctorId, String[] projection, String sortOrder);

    Cursor getMedicationsForPatient(long patientId, String[] projection, String sortOrder);

    long addMedicationForPatient(long patientId, long medication_id);

    int deleteMedicationForPatient(long patientId, long medication_id);
}
