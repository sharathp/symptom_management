package com.sharathp.symptom_management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.app.modules.ActivityModule;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * {@link android.app.Activity} base class for all activities.
 */
public abstract class BaseActivity extends FragmentActivity {
    private ObjectGraph mActivityScopeGraph;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
        // soon as possible.
        mActivityScopeGraph = null;
        super.onDestroy();
    }

    /**
     * Create a new Dagger ObjectGraph to add new dependencies using a plus operation and inject the
     * declared one in the activity. This new graph will be destroyed once the activity lifecycle
     * finish.
     *
     * This is the key of how to use Activity scope dependency injection.
     */
    private void injectDependencies() {
        final SymptomManagementApplication application = (SymptomManagementApplication) getApplication();
        final List<Object> activityScopeModules = getModules();
        activityScopeModules.add(new ActivityModule(this));
        mActivityScopeGraph = application.plus(activityScopeModules);
        inject(this);
    }

    /**
     * Method used to resolve dependencies provided by Dagger modules. Inject an object to provide
     * every @Inject annotation contained.
     *
     * @param object to inject.
     */
    public void inject(final Object object) {
        mActivityScopeGraph.inject(object);
    }

    /**
     * Get a list of Dagger modules with Activity scope needed to this Activity.
     *
     * @return modules with new dependencies to provide.
     */
    protected List<Object> getModules() {
        return new ArrayList<Object>();
    }

    protected void logout() {
        final Intent intent = new Intent(this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
