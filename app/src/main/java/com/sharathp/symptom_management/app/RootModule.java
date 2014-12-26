package com.sharathp.symptom_management.app;

import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                DatabaseModule.class, RestClientModule.class,
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
    @Named("Application")
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(@Named("Application") final Context context) {
        return LayoutInflater.from(context);
    }
}