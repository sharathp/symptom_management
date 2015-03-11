package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.sharathp.symptom_management.dao.PatientCheckInDao;
import com.sharathp.symptom_management.data.provider.contract.NamedCheckInContract;
import com.sharathp.symptom_management.data.sqlite.table.DoctorTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientCheckInTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientTable;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DefaultPatientCheckInDao extends DefaultDao<PatientCheckIn> implements PatientCheckInDao {

    private static final String NAMED_CHECKIN_JOIN_TABLES =
            DoctorTable.TABLE_NAME + " d" +
            " INNER JOIN " + PatientTable.TABLE_NAME + " p ON d." + DoctorTable._ID + " = p." + PatientTable.COLUMN_DOCTOR_ID +
            " INNER JOIN " + PatientCheckInTable.TABLE_NAME + " c ON p." + PatientTable._ID + " = c." + PatientCheckInTable.COLUMN_PATIENT_ID;

    private static final Map<String, String> NAMED_CHECKIN_PROJECTION = new HashMap<String, String>();

    static {
        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry._ID,
                "c." + PatientCheckInTable._ID + " AS " + NamedCheckInContract.NamedCheckInEntry._ID);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_SERVER_ID,
                "c." + PatientCheckInTable.COLUMN_SERVER_ID + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_SERVER_ID);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_CHECKIN_TIME,
                "c." + PatientCheckInTable.COLUMN_CHECKIN_TIME + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_CHECKIN_TIME);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_PAIN,
                "c." + PatientCheckInTable.COLUMN_PAIN + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PAIN);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_EATING,
                "c." + PatientCheckInTable.COLUMN_EATING + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_EATING);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_MEDICATED,
                "c." + PatientCheckInTable.COLUMN_MEDICATED + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_MEDICATED);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_ID,
                "p." + PatientTable._ID + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_ID);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_FIRST_NAME,
                "p." + PatientTable.COLUMN_FIRST_NAME + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_FIRST_NAME);

        NAMED_CHECKIN_PROJECTION.put(NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_LAST_NAME,
                "p." + PatientTable.COLUMN_LAST_NAME + " AS " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_LAST_NAME);
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
        sqLiteQueryBuilder.setTables(NAMED_CHECKIN_JOIN_TABLES);
        sqLiteQueryBuilder.setProjectionMap(NAMED_CHECKIN_PROJECTION);

        return sqLiteQueryBuilder.query(
                mDatabase,
                projection,
                "d." + DoctorTable._ID + " = ?",
                new String[] {Long.toString(doctorId)},
                null,
                null,
                NamedCheckInContract.NamedCheckInEntry.COLUMN_CHECKIN_TIME + " DESC",
                String.valueOf(numEntries)
        );
    }

    @Override
    public Cursor getAllPatientsLastCheckInForDoctor(final long doctorId, final String[] projection) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(NAMED_CHECKIN_JOIN_TABLES);
        sqLiteQueryBuilder.setProjectionMap(NAMED_CHECKIN_PROJECTION);

        // TODO - fix this query
        return sqLiteQueryBuilder.query(
                mDatabase,
                projection,
                "d." + DoctorTable._ID + " = ?",
                new String[] {Long.toString(doctorId)},
                null,
                null,
                NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_FIRST_NAME + ", " + NamedCheckInContract.NamedCheckInEntry.COLUMN_PATIENT_LAST_NAME
        );
    }
}