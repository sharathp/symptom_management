package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.view.LayoutInflater;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.service.PatientService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    LayoutInflater provideLayoutInflater(@ForApplication final Context context) {
        return LayoutInflater.from(context);
    }
}