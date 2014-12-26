package com.sharathp.symptom_management.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.data.SymptomManagementSQLiteHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true, complete = false)
public class DatabaseModule {

    @Provides
    @Singleton
    SQLiteDatabase provideSQLiteDatabase(@Named("Application") final Context context) {
        final SymptomManagementSQLiteHelper dbHelper = new SymptomManagementSQLiteHelper(context);
        return dbHelper.getWritableDatabase();
    }
}