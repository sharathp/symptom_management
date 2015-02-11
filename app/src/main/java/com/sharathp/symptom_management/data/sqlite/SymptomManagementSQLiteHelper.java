package com.sharathp.symptom_management.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sharathp.symptom_management.data.sqlite.table.CheckinMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.DoctorTable;
import com.sharathp.symptom_management.data.sqlite.table.MedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientCheckInTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientMedicationTable;
import com.sharathp.symptom_management.data.sqlite.table.PatientTable;
import com.sharathp.symptom_management.data.sqlite.table.ReminderTable;
import com.sharathp.symptom_management.model.PatientCheckIn;

/**
 * SQLiteHelper for Symptom Management application.
 */
public class SymptomManagementSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "symptom_management.db";

    public SymptomManagementSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(ReminderTable.SQL_CREATE);
        db.execSQL(PatientTable.SQL_CREATE);
        db.execSQL(DoctorTable.SQL_CREATE);
        db.execSQL(MedicationTable.SQL_CREATE);
        db.execSQL(PatientMedicationTable.SQL_CREATE);
        db.execSQL(PatientCheckInTable.SQL_CREATE);
        db.execSQL(CheckinMedicationTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SymptomManagementSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + ReminderTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PatientTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DoctorTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PatientMedicationTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PatientCheckInTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CheckinMedicationTable.TABLE_NAME);
        onCreate(db);
    }
}
