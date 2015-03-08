package com.sharathp.symptom_management.activity.prefs;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * {@link EditTextPreference} to be used for number preferences.
 */
public class IntEditTextPreference extends EditTextPreference {

    public IntEditTextPreference(final Context context) {
        super(context);
    }

    public IntEditTextPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreference(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(final String defaultReturnValue) {
        int defaultReturnIntValue = 0;
        if (defaultReturnValue != null) {
            defaultReturnIntValue = Integer.valueOf(defaultReturnValue);
        }
        return String.valueOf(getPersistedInt(defaultReturnIntValue));
    }

    @Override
    protected boolean persistString(final String value) {
        return persistInt(Integer.valueOf(value));
    }
}