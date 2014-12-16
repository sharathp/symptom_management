package com.sharathp.symptom_management.http;

import java.util.concurrent.Callable;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AsyncSymptomManagementAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    void loginAsDoctor(@Field("username") String username, @Field("password") String password,
                       Callable<Response> response);

    @FormUrlEncoded
    @POST("/oauth/token")
    void loginAsPatient(@Field("username") String username, @Field("password") String password,
                            Callable<Response> response);
}
