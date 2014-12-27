package com.sharathp.symptom_management.app.modules;

import android.app.Activity;
import android.content.Context;

import com.sharathp.symptom_management.activity.DoctorActivity;
import com.sharathp.symptom_management.activity.LoginActivity;
import com.sharathp.symptom_management.activity.MainActivity;
import com.sharathp.symptom_management.activity.PatientActivity;
import com.sharathp.symptom_management.app.ForActivity;
import com.sharathp.symptom_management.fragment.DoctorFragment;
import com.sharathp.symptom_management.fragment.LoginFragment;
import com.sharathp.symptom_management.fragment.PatientFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide some common activity scope dependencies as @ActivityContext.
 * This module is going to be added to the graph generated for every activity while the activity
 * creation lifecycle.
 */
@Module(library = true,
        injects = {
                DoctorActivity.class,
                LoginActivity.class,
                MainActivity.class,
                PatientActivity.class,
                DoctorFragment.class,
                LoginFragment.class,
                PatientFragment.class
        },
        complete = false)
public final class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @ForActivity
    @Provides
    @Singleton
    Context provideActivityContext() {
        return activity;
    }
}
