package com.sharathp.symptom_management.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import timber.log.Timber;

/**
 * Represents the session of this logged-in user.
 */
public class Session {
    private static final String TAG = Session.class.getSimpleName();

    // Preference keys
    private static final String KEY = "session";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_TYPE = "user_type";
    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";
    private static final String _ID = "_id";

    // User-type constants
    public static final int DOCTOR = 1;
    public static final int PATIENT = 2;

    // Session fields
    private String mUserName;
    private String mAccessToken;
    private int mUserType;
    private String mUserId;
    private long m_id;

    private Session() {
        // singleton
    }

    private static Session instance;

    /**
     * Load the session data from disk.
     *
     * @param context
     * @return session
     */
    public static synchronized Session restore(final Context context) {
        if(instance != null) {
            return instance;
        }

        final SharedPreferences prefs = getSharedPreferences(context);
        final String accessToken = prefs.getString(ACCESS_TOKEN, null);
        final String userName = prefs.getString(USER_NAME, null);
        final int userType = prefs.getInt(USER_TYPE, -1);
        final String userId = prefs.getString(USER_ID, null);
        final long _id = prefs.getLong(_ID, -1);

        if(!isValid(userName, accessToken, userType, userId)) {
            clearSavedSession(context);
            return null;
        }

        instance = new Session();
        instance.mUserName = userName;
        instance.mUserType = userType;
        instance.mAccessToken = accessToken;
        instance.mUserId = userId;
        instance.m_id = _id;
        return instance;
    }

    /**
     * Create session.
     *
     * @param context
     * @param userName
     * @param accessToken
     * @param userType
     */
    public static synchronized boolean saveSession(final Context context, final String userName,
                               final String accessToken, final int userType, final String userId) {
        if(!isValid(userName, accessToken, userType, userId)) {
            final StringBuilder sb = new StringBuilder("Invalid session parameters: ");
            sb.append("user-name: ").append(userName).append("; user-type: ").append(userType)
            .append("; access-token: ").append(accessToken)
            .append("; user-id: ").append(userId);
            Timber.e(TAG, sb.toString());
            return false;
        }
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        return editor.putString(USER_NAME, userName)
                .putString(ACCESS_TOKEN, accessToken)
                .putInt(USER_TYPE, userType)
                .putString(USER_ID, userId)
                .commit();
    }

    private static boolean isValid(final String userName, final String accessToken,
                                   final int userType, final String userId) {
        return (userName != null && (userType == DOCTOR || userType == PATIENT)
                && accessToken != null && userId != null);
    }

    public void set_id(final Context context, final Long _id) {
        if(_id == null) {
            return;
        }

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(_ID, _id).commit();
        m_id = _id;
    }

    /**
     * Clears the saved session data.
     */
    public static synchronized void clearSavedSession(final Context context) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear().commit();
        instance = null;
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public String getUserName() {
        return mUserName;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public int getUserType() {
        return mUserType;
    }

    public String getUserId() {
        return mUserId;
    }

    public long get_id() {
        return m_id;
    }

    public boolean isDoctor() {
        return DOCTOR == mUserType;
    }

    public boolean isPatient() {
        return PATIENT == mUserType;
    }
}
