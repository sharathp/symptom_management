package com.sharathp.symptom_management.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.app.modules.ActivityModule;
import com.sharathp.symptom_management.app.SymptomManagementApplication;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * {@link Activity} base class for single fragment activities.
 */
public abstract class SingleFragmentActivity extends Activity {
    private ObjectGraph activityScopeGraph;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        injectDependencies();
        final Fragment fragment = getFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    protected abstract Fragment getFragment();

    @Override
    protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
        // soon as possible.
        activityScopeGraph = null;
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
        activityScopeGraph = application.plus(activityScopeModules);
        inject(this);
    }

    /**
     * Method used to resolve dependencies provided by Dagger modules. Inject an object to provide
     * every @Inject annotation contained.
     *
     * @param object to inject.
     */
    public void inject(final Object object) {
        activityScopeGraph.inject(object);
    }

    /**
     * Get a list of Dagger modules with Activity scope needed to this Activity.
     *
     * @return modules with new dependencies to provide.
     */
    protected List<Object> getModules() {
        return new ArrayList<Object>();
    }
}
