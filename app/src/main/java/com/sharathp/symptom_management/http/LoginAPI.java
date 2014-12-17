package com.sharathp.symptom_management.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface LoginAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    void login(@Field("username") String username, @Field("password") String password,
               Callback<Void> callback);
}
