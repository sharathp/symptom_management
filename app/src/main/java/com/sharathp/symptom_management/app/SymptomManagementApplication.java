package com.sharathp.symptom_management.app;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Main Application for the entire app.
 */
public class SymptomManagementApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules());
    }

    public void inject(final Object object) {
        objectGraph.inject(object);
    }

    private Object[] getModules() {
        return new Object[] {new RootModule(this)};
    }
}
