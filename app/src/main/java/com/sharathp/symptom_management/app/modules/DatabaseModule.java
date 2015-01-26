package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.dao.Dao;
import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.dao.impl.DefaultDao;
import com.sharathp.symptom_management.dao.impl.DefaultPatientDao;
import com.sharathp.symptom_management.data.DoctorContract;
import com.sharathp.symptom_management.data.MedicationContract;
import com.sharathp.symptom_management.data.PatientContract;
import com.sharathp.symptom_management.data.ReminderContract;
import com.sharathp.symptom_management.data.SymptomManagementProvider;
import com.sharathp.symptom_management.data.SymptomManagementSQLiteHelper;
import com.sharathp.symptom_management.model.Doctor;
import com.sharathp.symptom_management.model.Medication;
import com.sharathp.symptom_management.model.Reminder;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true,
        injects = {
            SymptomManagementProvider.class,
            DefaultDao.class
        },
        complete = false)
public class DatabaseModule {

    @Provides
    @Singleton
    SQLiteDatabase provideSQLiteDatabase(@ForApplication final Context context) {
        final SymptomManagementSQLiteHelper dbHelper = new SymptomManagementSQLiteHelper(context);
        return dbHelper.getWritableDatabase();
    }

    @Named("doctor")
    @Provides
    @Singleton
    Dao<Doctor> provideDoctorDao(final DefaultDao<Doctor> configured) {
        configured.setTable(DoctorContract.DoctorEntry.TABLE_NAME);
        return configured;
    }

    @Named("patient")
    @Provides
    @Singleton
    PatientDao providePatientDao(final DefaultPatientDao configured) {
        configured.setTable(PatientContract.PatientEntry.TABLE_NAME);
        return configured;
    }

    @Named("medication")
    @Provides
    @Singleton
    Dao<Medication> provideMedicationDao(final DefaultDao<Medication> configured) {
        configured.setTable(MedicationContract.MedicationEntry.TABLE_NAME);
        return configured;
    }

    @Named("reminder")
    @Provides
    @Singleton
    Dao<Reminder> provideReminderDao(final DefaultDao<Reminder> configured) {
        configured.setTable(ReminderContract.ReminderEntry.TABLE_NAME);
        return configured;
    }
}