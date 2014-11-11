package com.sharathp.symptom_management.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Represents the session of this logged-in user.
 */
public class Session {
    // Preference keys
    private static final String KEY = "session";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_TYPE = "user_type";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";

    // User-type constants
    private static final int DOCTOR = 1;
    private static final int PATIENT = 2;

    // Session fields
    private String userName;
    private String userId;
    private String accessToken;
    private int userType;

    private Session() {
        // singleton
    }

    private static Session instance;

    /**
     * Load the session data from disk.
     */
    public static synchronized Session restore(final Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);

        final String accessToken = prefs.getString(ACCESS_TOKEN, null);

        if(accessToken == null) {
            return null;
        }

        final String userId = prefs.getString(USER_ID, null);
        final String userName = prefs.getString(USER_NAME, null);
        final int userType = prefs.getInt(USER_TYPE, 0);

        instance = new Session();
        instance.userId = userId;
        instance.userName = userName;
        instance.userType = userType;
        instance.accessToken = accessToken;

        return instance;
    }

    /**
     * Clears the saved session data.
     */
    public static synchronized void clearSavedSession(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
        instance = null;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getUserType() {
        return userType;
    }

    public boolean isDoctor() {
        return DOCTOR == userType;
    }

    public boolean isPatient() {
        return PATIENT == userType;
    }
}
