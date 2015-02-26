package com.sharathp.symptom_management.app.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.service.PatientService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module(
        includes = {
            DatabaseModule.class,
            RestClientModule.class,
            ServiceModule.class
        },
        injects = {
            SymptomManagementApplication.class
        },
        library = true)
public class RootModule {
    private final SymptomManagementApplication mApplication;

    /**
     * @param application - SymptomManagementApplication
     */
    public RootModule(final SymptomManagementApplication application) {
        this.mApplication = application;
    }

    @Provides
    @ForApplication
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@ForApplication final Context context) {
        return context.getSharedPreferences("symptom_management", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(@ForApplication final Context context) {
        return LayoutInflater.from(context);
    }
}