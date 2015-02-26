package com.sharathp.symptom_management.app.model;

import com.sharathp.symptom_management.model.prefs.StringPreference;

import retrofit.Endpoint;

public class StringPreferenceEndpoint implements Endpoint {
    private static final String DEFAULT_NAME = "default";

    private StringPreference mStringPreference;
    private String mName;

    public StringPreferenceEndpoint(final StringPreference stringPreference) {
        this(stringPreference, DEFAULT_NAME);
    }

    public StringPreferenceEndpoint(final StringPreference stringPreference, final String name) {
        mStringPreference = stringPreference;
        mName = name;
    }

    @Override
    public String getUrl() {
        return mStringPreference.get();
    }

    @Override
    public String getName() {
        return DEFAULT_NAME;
    }
}