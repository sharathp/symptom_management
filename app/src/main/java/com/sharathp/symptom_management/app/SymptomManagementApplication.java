package com.sharathp.symptom_management.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.data.SymptomManagementSQLiteHelper;
import com.sharathp.symptom_management.http.SymptomManagementAPI;

/**
 *
 */
public class SymptomManagementApplication extends Application {
    private static SymptomManagementApplication singleton;
    private SymptomManagementSQLiteHelper dbHelper;
    private Thread uiThread;
    private SymptomManagementAPI symptomManagementAPI;

    public static SymptomManagementApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        uiThread = Thread.currentThread();
        dbHelper = new SymptomManagementSQLiteHelper(this);
    }

    public SQLiteDatabase getDb() {
        if (Thread.currentThread().equals(uiThread)) {
            throw new RuntimeException("Database opened on main thread"); }
        return dbHelper.getWritableDatabase();
    }

    public SymptomManagementAPI getSymptomManagementAPI() {
        return symptomManagementAPI;
    }
}
