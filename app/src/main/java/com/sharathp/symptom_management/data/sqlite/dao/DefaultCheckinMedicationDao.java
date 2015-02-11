package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.sharathp.symptom_management.dao.CheckinMedicationDao;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.data.sqlite.table.CheckinMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.MedicationTable;
import com.sharathp.symptom_management.model.MedicationIntake;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DefaultCheckinMedicationDao extends DefaultDao<MedicationIntake> implements CheckinMedicationDao {

    private static final String CHECKIN_MEDICATION_MEDICATION_JOIN_TABLES =
            CheckinMedicationTable.TABLE_NAME + " c INNER JOIN " +
                    MedicationTable.TABLE_NAME + " m ON c." + CheckinMedicationTable.COLUMN_MEDICATION_ID +
                    " = m." + MedicationTable._ID;

    private static final Map<String, String> CHECKIN_MEDICATION_MEDICATION_PROJECTION = new HashMap<String, String>();

    static {
        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry._ID,
                "c." + CheckinMedicationTable._ID + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry._ID);

        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_ID,
                "c." + CheckinMedicationTable.COLUMN_MEDICATION_ID + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_ID);

        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_TIME,
                "c." + CheckinMedicationTable.COLUMN_MEDICATION_TIME + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_TIME);

        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_NAME,
                "m." + MedicationTable.COLUMN_NAME + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_NAME);

        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_SERVER_ID,
                "m." + MedicationTable.COLUMN_SERVER_ID + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_SERVER_ID);

        CHECKIN_MEDICATION_MEDICATION_PROJECTION.put(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_CHECKIN_ID,
                "c." + CheckinMedicationTable.COLUMN_CHECKIN_ID + " AS " + PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_CHECKIN_ID);
    }

    @Inject
    public DefaultCheckinMedicationDao() {
        mTable = CheckinMedicationTable.TABLE_NAME;
    }

    @Override
    public Cursor getAllMedicationsForCheckin(final long checkinId, final String[] projection, final String sortOrder) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(CHECKIN_MEDICATION_MEDICATION_JOIN_TABLES);
        sqLiteQueryBuilder.setProjectionMap(CHECKIN_MEDICATION_MEDICATION_PROJECTION);

        return sqLiteQueryBuilder.query(
                mDatabase,
                projection,
                CheckinMedicationTable.COLUMN_CHECKIN_ID + " = ?",
                new String[] {Long.toString(checkinId)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public long createCheckInMedication(final long checkInId, final ContentValues values) {
        final ContentValues dbContentValues = new ContentValues();
        dbContentValues.put(CheckinMedicationTable.COLUMN_CHECKIN_ID, checkInId);
        dbContentValues.put(CheckinMedicationTable.COLUMN_MEDICATION_ID, (Long) values.get(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_ID));
        dbContentValues.put(CheckinMedicationTable.COLUMN_MEDICATION_TIME, (Long) values.get(PatientCheckInContract.PatientCheckInMedicationIntakeEntry.COLUMN_MEDICATION_TIME));
        return mDatabase.insert(mTable, null, dbContentValues);
    }
}