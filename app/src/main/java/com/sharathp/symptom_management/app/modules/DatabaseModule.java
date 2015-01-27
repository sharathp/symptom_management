package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.dao.impl.DefaultDao;
import com.sharathp.symptom_management.dao.impl.DefaultDoctorDao;
import com.sharathp.symptom_management.dao.impl.DefaultMedicationDao;
import com.sharathp.symptom_management.dao.impl.DefaultPatientDao;
import com.sharathp.symptom_management.dao.impl.DefaultReminderDao;
import com.sharathp.symptom_management.data.SymptomManagementProvider;
import com.sharathp.symptom_management.data.SymptomManagementSQLiteHelper;

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
}