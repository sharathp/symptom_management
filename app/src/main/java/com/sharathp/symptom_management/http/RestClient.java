package com.sharathp.symptom_management.http;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {
    private static SymptomManagementAPI API;
    private static AsyncSymptomManagementAPI ASYNC_API;

    private static String ROOT = "http://192.168.0.12";

    static {
        setupRestClients();
    }

    private RestClient() {
        // no-op
    }

    private static void setupRestClients() {
        final RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        final RestAdapter restAdapter = builder.build();
        API = restAdapter.create(SymptomManagementAPI.class);
        ASYNC_API = restAdapter.create(AsyncSymptomManagementAPI.class);
    }

}
