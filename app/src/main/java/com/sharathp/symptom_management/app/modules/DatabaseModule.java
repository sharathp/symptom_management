package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.dao.PatientCheckInDao;
import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.data.sqlite.dao.DefaultDoctorDao;
import com.sharathp.symptom_management.data.sqlite.dao.DefaultMedicationDao;
import com.sharathp.symptom_management.data.sqlite.dao.DefaultPatientCheckInDao;
import com.sharathp.symptom_management.data.sqlite.dao.DefaultPatientDao;
import com.sharathp.symptom_management.data.sqlite.dao.DefaultReminderDao;
import com.sharathp.symptom_management.data.provider.SymptomManagementProvider;
import com.sharathp.symptom_management.data.sqlite.SymptomManagementSQLiteHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

// TODO - remove all DAOs?
@Module(library = true,
        injects = {
            SymptomManagementProvider.class,
            DefaultDoctorDao.class,
            DefaultPatientDao.class,
            DefaultMedicationDao.class,
            DefaultReminderDao.class,
            DefaultPatientCheckInDao.class
        },
        complete = false)
public class DatabaseModule {

    @Provides
    @Singleton
    SQLiteDatabase provideSQLiteDatabase(@ForApplication final Context context) {
        final SymptomManagementSQLiteHelper dbHelper = new SymptomManagementSQLiteHelper(context);
        return dbHelper.getWritableDatabase();
    }

    @Provides
    @Singleton
    DoctorDao provideDoctorDao(final DefaultDoctorDao defaultDoctorDao) {
        return defaultDoctorDao;
    }

    @Provides
    @Singleton
    PatientDao providePatientDao(final DefaultPatientDao defaultPatientDao) {
        return defaultPatientDao;
    }

    @Provides
    @Singleton
    MedicationDao provideMedicationDao(final DefaultMedicationDao defaultMedicationDao) {
        return defaultMedicationDao;
    }

    @Provides
    @Singleton
    ReminderDao provideReminderDao(final DefaultReminderDao defaultReminderDao) {
        return defaultReminderDao;
    }

    @Provides
    @Singleton
    PatientCheckInDao providePatientCheckInDao(final DefaultPatientCheckInDao defaultPatientCheckInDao) {
        return defaultPatientCheckInDao;
    }
}