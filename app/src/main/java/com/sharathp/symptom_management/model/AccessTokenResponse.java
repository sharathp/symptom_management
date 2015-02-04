package com.sharathp.symptom_management.model;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {
    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("token_type")
    private String mTokenType;

    @SerializedName("refresh_token")
    private String mRrefreshToken;

    @SerializedName("expires_in")
    private long mExpiresIn;

    @SerializedName("scope")
    private String mScope;

    @SerializedName("user_id")
    private String mUserId;

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public void setTokenType(final String tokenType) {
        this.mTokenType = tokenType;
    }

    public String getRrefreshToken() {
        return mRrefreshToken;
    }

    public void setRrefreshToken(final String rrefreshToken) {
        this.mRrefreshToken = rrefreshToken;
    }

    public long getExpiresIn() {
        return mExpiresIn;
    }

    public void setExpiresIn(final long expiresIn) {
        this.mExpiresIn = expiresIn;
    }

    public String getScope() {
        return mScope;
    }

    public void setScope(final String scope) {
        this.mScope = scope;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }
}
