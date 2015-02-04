package com.sharathp.symptom_management.login;

import android.content.Context;
import android.content.SharedPreferences;

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
    private static final String SERVER_ID = "server_id";
    private static final String ID = "id";

    // User-type constants
    public static final int DOCTOR = 1;
    public static final int PATIENT = 2;

    // Session fields
    private String mUserName;
    private String mAccessToken;
    private int mUserType;
    private String mUserId;
    private long mId;

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
        final String userId = prefs.getString(SERVER_ID, null);
        final long id = prefs.getLong(ID, -1);

        if(!isValid(userName, accessToken, userType, userId)) {
            clearSavedSession(context);
            return null;
        }

        instance = new Session();
        instance.mUserName = userName;
        instance.mUserType = userType;
        instance.mAccessToken = accessToken;
        instance.mUserId = userId;
        instance.mId = id;
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
                               final String accessToken, final int userType, final String serverId) {
        if(!isValid(userName, accessToken, userType, serverId)) {
            final StringBuilder sb = new StringBuilder("Invalid session parameters: ");
            sb.append("user-name: ").append(userName).append("; user-type: ").append(userType)
            .append("; access-token: ").append(accessToken)
            .append("; user-id: ").append(serverId);
            Timber.e(TAG, sb.toString());
            return false;
        }
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        return editor.putString(USER_NAME, userName)
                .putString(ACCESS_TOKEN, accessToken)
                .putInt(USER_TYPE, userType)
                .putString(SERVER_ID, serverId)
                .commit();
    }

    private static boolean isValid(final String userName, final String accessToken,
                                   final int userType, final String serverId) {
        return (userName != null && (userType == DOCTOR || userType == PATIENT)
                && accessToken != null && serverId != null);
    }

    public void setId(final Context context, final Long id) {
        if(id == null) {
            return;
        }

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(ID, id).commit();
        mId = id;
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

    public String getServerId() {
        return mUserId;
    }

    public long getId() {
        return mId;
    }

    public boolean isDoctor() {
        return DOCTOR == mUserType;
    }

    public boolean isPatient() {
        return PATIENT == mUserType;
    }
}
