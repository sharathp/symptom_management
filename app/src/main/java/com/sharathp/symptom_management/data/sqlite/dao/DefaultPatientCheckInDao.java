package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharathp.symptom_management.dao.PatientCheckInDao;
import com.sharathp.symptom_management.data.sqlite.table.PatientCheckInTable;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultPatientCheckInDao extends DefaultDao<PatientCheckIn> implements PatientCheckInDao {

    @Inject
    public DefaultPatientCheckInDao() {
        mTable = PatientCheckInTable.TABLE_NAME;
    }

    @Override
    public Cursor getPatientCheckIns(final long patientId, final String[] projection, final String sortOrder) {
        return mDatabase.query(
                mTable,
                projection,
                PatientCheckInTable.COLUMN_PATIENT_ID + " = ?",
                new String[] {Long.toString(patientId)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public long createPatientCheckIn(final long patientId, final ContentValues values) {
        values.put(PatientCheckInTable.COLUMN_PATIENT_ID, patientId);
        return mDatabase.insert(mTable, null, values);
    }

    @Override
    public int deletePatientCheckIns(final long patientId, final String selection, final String[] selectionArgs) {
        final String patientClause = PatientCheckInTable.COLUMN_PATIENT_ID + " = ?";
        String modifiedSelection = null;
        String[] modifiedSelectionArgs = null;

        if(selection == null) {
            modifiedSelection = patientClause;
            modifiedSelectionArgs = new String[]{Long.toString(patientId)};
        } else {
            modifiedSelection = selection + " AND " + patientClause;
            modifiedSelectionArgs = Arrays.copyOf(selectionArgs, selectionArgs.length + 1);
            modifiedSelectionArgs[modifiedSelectionArgs.length - 1] = Long.toString(patientId);
        }

        return mDatabase.delete(mTable, modifiedSelection, modifiedSelectionArgs);
    }
}