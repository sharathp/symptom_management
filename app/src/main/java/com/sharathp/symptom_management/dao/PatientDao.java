package com.sharathp.symptom_management.dao;

import android.database.Cursor;

import com.sharathp.symptom_management.model.Patient;

public interface PatientDao extends Dao<Patient> {

    Cursor getPatientsForDoctor(long doctor_id, String[] projection, String sortOrder);
}
