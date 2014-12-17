package com.sharathp.symptom_management.http;

import android.util.Base64;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {
    private static SymptomManagementAPI API;
    private static AsyncSymptomManagementAPI ASYNC_API;
    private static LoginAPI DOCTOR_LOGIN_API;
    private static LoginAPI PATIENT_LOGIN_API;

    private static String ROOT = "http://192.168.0.109:8080";
    private static String AUTHORIZATION_HEADER = "Authorization";
    private static String AUTHORIZATION_BASIC = "Basic";
    public static String GRANT_TYPE_PASSWORD = "password";

    private static String DOCTOR_CLIENT_ID = "mobile_doctor";
    private static String DOCTOR_CLIENT_SECRET = "mobile_doctor";

    private static String PATIENT_CLIENT_ID = "mobile_patient";
    private static String PATIENT_CLIENT_SECRET = "mobile_patient";

    static {
        setupRestClients();
    }

    private RestClient() {
        // no-op
    }

    public static LoginAPI doctorLoginApi() {
        return DOCTOR_LOGIN_API;
    }

    public static LoginAPI patientLoginApi() {
        return PATIENT_LOGIN_API;
    }

    private static void setupRestClients() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final OkClient okClient = new OkClient(okHttpClient);

        // TODO - if 401 is returned, find a way to logout..

        final RestAdapter commonAdapter = restAdapterBuilder(okClient).build();
        API = commonAdapter.create(SymptomManagementAPI.class);
        ASYNC_API = commonAdapter.create(AsyncSymptomManagementAPI.class);

        final RestAdapter.Builder doctorLoginAdapter = restAdapterBuilder(okClient);
        doctorLoginAdapter.setRequestInterceptor(
                loginInterceptor(DOCTOR_CLIENT_ID, DOCTOR_CLIENT_SECRET));

        DOCTOR_LOGIN_API = doctorLoginAdapter.build().create(LoginAPI.class);

        final RestAdapter.Builder patientLoginAdapter = restAdapterBuilder(okClient);
        patientLoginAdapter.setRequestInterceptor(
                loginInterceptor(PATIENT_CLIENT_ID, PATIENT_CLIENT_SECRET));

        PATIENT_LOGIN_API = patientLoginAdapter.build().create(LoginAPI.class);
    }

    private static RestAdapter.Builder restAdapterBuilder(final OkClient okClient) {
        final RestAdapter.Builder commonBuilder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(okClient)
                .setLogLevel(RestAdapter.LogLevel.FULL);
        return commonBuilder;
    }

    private static RequestInterceptor loginInterceptor(final String clientId,
                                                       final String clientSecret) {
        final String base64Encoded = Base64.encodeToString(
                (clientId + ":" + clientSecret).getBytes(), Base64.NO_WRAP);
        final String authorizationHeader = AUTHORIZATION_BASIC + " " + base64Encoded;
        return new RequestInterceptor() {
            @Override
            public void intercept(final RequestFacade request) {
                request.addHeader(AUTHORIZATION_HEADER, authorizationHeader);
            }
        };
    }
}
