package com.sharathp.symptom_management.app;

import android.app.Application;

import com.sharathp.symptom_management.BuildConfig;
import com.sharathp.symptom_management.app.modules.RootModule;

import java.util.List;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Main Application for the entire app.
 */
public class SymptomManagementApplication extends Application {
    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDependencyInjector();
    }

    /**
     * Inject every dependency declared in the object with the @Inject annotation if the dependency
     * has been already declared in a module and already initialized by Dagger.
     *
     * @param object to inject.
     */
    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    /**
     * Extend the dependency container graph will new dependencies provided by the modules passed as
     * arguments.
     *
     * @param modules used to populate the dependency container.
     */
    public ObjectGraph plus(List<Object> modules) {
        if (modules == null) {
            throw new IllegalArgumentException(
                    "You can't plus a null module, review your getModules() implementation");
        }
        return mObjectGraph.plus(modules.toArray());
    }

    private void configureLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initializeDependencyInjector() {
        mObjectGraph = ObjectGraph.create(new RootModule(this));
        mObjectGraph.inject(this);
        mObjectGraph.injectStatics();
    }
}
