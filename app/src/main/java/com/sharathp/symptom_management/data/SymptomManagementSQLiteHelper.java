package com.sharathp.symptom_management.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        db.execSQL(SymptomManagementContract.ReminderEntry.SQL_CREATE);
        db.execSQL(PatientContract.PatientEntry.SQL_CREATE);
        db.execSQL(DoctorContract.DoctorEntry.SQL_CREATE);
        db.execSQL(MedicationContract.MedicationEntry.SQL_CREATE);
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
        db.execSQL("DROP TABLE IF EXISTS " + SymptomManagementContract.ReminderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PatientContract.PatientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DoctorContract.DoctorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationContract.MedicationEntry.TABLE_NAME);
        onCreate(db);
    }
}
