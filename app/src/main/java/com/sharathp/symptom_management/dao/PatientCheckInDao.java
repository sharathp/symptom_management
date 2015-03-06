package com.sharathp.symptom_management.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharathp.symptom_management.model.PatientCheckIn;

public interface PatientCheckInDao extends Dao<PatientCheckIn> {

    Cursor getPatientCheckIns(long patientId, String[] projection, String sortOrder);

    long createPatientCheckIn(long patientId, ContentValues values);

    int deletePatientCheckIns(long patientId, String selection, String[] selectionArgs);

    Cursor getRecentCheckInsForDoctor(long doctorId, String[] projection, String sortOrder);
}