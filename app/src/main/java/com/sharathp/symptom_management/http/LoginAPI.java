package com.sharathp.symptom_management.http;

import com.sharathp.symptom_management.model.AccessTokenResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface LoginAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    void login(@Field("username") String username,
               @Field("password") String password,
               @Field("grant_type") String grantType,
               Callback<AccessTokenResponse> callback);
}
