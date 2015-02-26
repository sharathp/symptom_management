package com.sharathp.symptom_management.model.prefs;

import android.content.SharedPreferences;

public class StringPreference {
    private final SharedPreferences preferences;
    private final String key;
    private final String defaultValue;

    public StringPreference(final SharedPreferences preferences, final String key) {
        this(preferences, key, null);
    }

    public StringPreference(final SharedPreferences preferences, final String key, final String defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String get() {
        return preferences.getString(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}
