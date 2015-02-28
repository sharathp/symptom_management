package com.sharathp.symptom_management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.app.modules.ActivityModule;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * {@link android.app.Activity} base class for all activities.
 */
public abstract class BaseActivity extends ActionBarActivity {
    private ObjectGraph mActivityScopeGraph;
    // TODO - modify this to be injected
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        setContentView(getLayoutResource());
        setToolbarAsActionBar();
        initializeDrawer();
    }

    @Override
    protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
        // soon as possible.
        mActivityScopeGraph = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if(mDrawer != null) {
                    mDrawer.openDrawer(Gravity.START);
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutResource();

    protected void setActionBarIcon(final int iconRes) {
        mToolbar.setNavigationIcon(iconRes);
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

    private void setToolbarAsActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void initializeDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        if(mDrawer != null) {
            mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
            setActionBarIcon(R.drawable.ic_ab_drawer);
        }
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
}