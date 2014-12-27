package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.view.LayoutInflater;

import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.app.SymptomManagementApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
            DatabaseModule.class,
            RestClientModule.class
        },
        injects = {
            SymptomManagementApplication.class
        },
        library = true)
public class RootModule {
    private final SymptomManagementApplication application;

    /**
     * @param application - SymptomManagementApplication
     */
    public RootModule(final SymptomManagementApplication application) {
        this.application = application;
    }

    @Provides
    @ForApplication
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(@ForApplication final Context context) {
        return LayoutInflater.from(context);
    }
}