package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.data.SymptomManagementProvider;
import com.sharathp.symptom_management.data.SymptomManagementSQLiteHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true,
        injects = {
            SymptomManagementProvider.class
        },
        complete = false)
public class DatabaseModule {

    @Provides
    @Singleton
    SQLiteDatabase provideSQLiteDatabase(@ForApplication final Context context) {
        final SymptomManagementSQLiteHelper dbHelper = new SymptomManagementSQLiteHelper(context);
        return dbHelper.getWritableDatabase();
    }
}