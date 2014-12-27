package com.sharathp.symptom_management.app;

import timber.log.Timber;

/**
 *
 */
public class CrashReportingTree extends Timber.HollowTree {

    @Override
    public void i(String message, Object... args) {
        // TODO e.g., Crashlytics.log(String.format(message, args));
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        i(message, args); // Just add to the log.
    }

    @Override
    public void e(String message, Object... args) {
        i("ERROR: " + message, args); // Just add to the log.
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        e(message, args);
        // TODO e.g., Crashlytics.logException(t);
    }
}
