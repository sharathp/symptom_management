package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.sharathp.symptom_management.dao.PatientCheckInDao;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.data.provider.contract.RecentCheckInContract;
import com.sharathp.symptom_management.data.sqlite.table.CheckinMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.DoctorTable;
import com.sharathp.symptom_management.data.sqlite.table.MedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientCheckInTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientTable;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DefaultPatientCheckInDao extends DefaultDao<PatientCheckIn> implements PatientCheckInDao {

    private static final String RECENT_CHECKIN_JOIN_TABLES =
            DoctorTable.TABLE_NAME + " d" +
            " INNER JOIN " + PatientTable.TABLE_NAME + " p ON d." + DoctorTable._ID + " = p." + PatientTable.COLUMN_DOCTOR_ID +
            " INNER JOIN " + PatientCheckInTable.TABLE_NAME + " c ON p." + PatientTable._ID + " = c." + PatientCheckInTable.COLUMN_PATIENT_ID;

    private static final Map<String, String> RECENT_CHECKIN_PROJECTION = new HashMap<String, String>();

    // this is for internal purposes only and therefore doesn't need to be exposed in the contract..
//    private static final String COLUMN_DOCTOR_ID = "doctor_id";


    static {
        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry._ID,
                "c." + PatientCheckInTable._ID + " AS " + RecentCheckInContract.RecentCheckInEntry._ID);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_SERVER_ID,
                "c." + PatientCheckInTable.COLUMN_SERVER_ID + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_SERVER_ID);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_CHECKIN_TIME,
                "c." + PatientCheckInTable.COLUMN_CHECKIN_TIME + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_CHECKIN_TIME);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_PAIN,
                "c." + PatientCheckInTable.COLUMN_PAIN + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_PAIN);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_EATING,
                "c." + PatientCheckInTable.COLUMN_EATING + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_EATING);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_MEDICATED,
                "c." + PatientCheckInTable.COLUMN_MEDICATED + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_MEDICATED);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_ID,
                "p." + PatientTable._ID + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_ID);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_FIRST_NAME,
                "p." + PatientTable.COLUMN_FIRST_NAME + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_FIRST_NAME);

        RECENT_CHECKIN_PROJECTION.put(RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_LAST_NAME,
                "p." + PatientTable.COLUMN_LAST_NAME + " AS " + RecentCheckInContract.RecentCheckInEntry.COLUMN_PATIENT_LAST_NAME);

//        RECENT_CHECKIN_PROJECTION.put(COLUMN_DOCTOR_ID,
//                "d." + DoctorTable._ID + " AS " + COLUMN_DOCTOR_ID);
    }

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

    @Override
    public Cursor getRecentCheckInsForDoctor(final long doctorId, final String[] projection, final int numEntries) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(RECENT_CHECKIN_JOIN_TABLES);
        sqLiteQueryBuilder.setProjectionMap(RECENT_CHECKIN_PROJECTION);

        return sqLiteQueryBuilder.query(
                mDatabase,
                projection,
                DoctorTable._ID + " = ?",
                new String[] {Long.toString(doctorId)},
                null,
                null,
                RecentCheckInContract.RecentCheckInEntry.COLUMN_CHECKIN_TIME + " DESC",
                String.valueOf(numEntries)
        );
    }
}