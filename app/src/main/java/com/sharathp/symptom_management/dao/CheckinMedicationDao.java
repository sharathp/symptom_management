package com.sharathp.symptom_management.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharathp.symptom_management.model.MedicationIntake;

public interface CheckinMedicationDao extends Dao<MedicationIntake> {

    Cursor getAllMedicationsForCheckin(long checkinId, String[] projection, String sortOrder);

    long createCheckInMedication(long checkInId, ContentValues values);
}
