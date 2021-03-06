package com.sharathp.symptom_management.app.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharathp.symptom_management.app.ForApplication;
import com.sharathp.symptom_management.app.model.StringPreferenceEndpoint;
import com.sharathp.symptom_management.http.LoginAPI;
import com.sharathp.symptom_management.http.SymptomManagementAPI;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.prefs.StringPreference;
import com.sharathp.symptom_management.util.Constants;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(library = true,
        complete = false)
public class RestClientModule {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_BASIC = "Basic";
    private static final String AUTHORIZATION_BEARER = "BEARER";
    public static final String GRANT_TYPE_PASSWORD = "password";

    private static final String DOCTOR_CLIENT_ID = "mobile_doctor";
    private static final String DOCTOR_CLIENT_SECRET = "mobile_doctor";

    private static final String PATIENT_CLIENT_ID = "mobile_patient";
    private static final String PATIENT_CLIENT_SECRET = "mobile_patient";

    private static final String DEV_ROOT = "http://192.168.0.102:8080";

    @Inject
    @ForApplication
    Context mContext;

    @Provides
    @Singleton
    StringPreference provideEndpointPreference(final SharedPreferences preferences) {
        return new StringPreference(preferences, "api_endpoint", DEV_ROOT);
    }

    @Provides
    @Singleton
    StringPreferenceEndpoint provideEndpoint(final StringPreference preference) {
        return new StringPreferenceEndpoint(preference);
    }

    @Provides
    @Singleton
    SymptomManagementAPI provideSymptomManagementAPI(final OkClient okClient,
                                                     final StringPreferenceEndpoint endpoint) {
        final RestAdapter.Builder commonAdapter = restAdapterBuilder(okClient, endpoint);
        commonAdapter.setRequestInterceptor(authorizationInterceptor());
        return commonAdapter.build().create(SymptomManagementAPI.class);
    }

    @Provides
    @Singleton
    @Named("DoctorLoginApi")
    LoginAPI provideDoctorLoginApi(final OkClient okClient,
                                   final StringPreferenceEndpoint endpoint) {
        final RestAdapter.Builder doctorLoginAdapter = restAdapterBuilder(okClient, endpoint);
        doctorLoginAdapter.setRequestInterceptor(
                loginInterceptor(DOCTOR_CLIENT_ID, DOCTOR_CLIENT_SECRET));
        return doctorLoginAdapter.build().create(LoginAPI.class);
    }

    @Provides
    @Singleton
    @Named("PatientLoginApi")
    LoginAPI providePatientLoginApi(final OkClient okClient,
                                    final StringPreferenceEndpoint endpoint) {
        final RestAdapter.Builder patientLoginAdapter = restAdapterBuilder(okClient, endpoint);
        patientLoginAdapter.setRequestInterceptor(
                loginInterceptor(PATIENT_CLIENT_ID, PATIENT_CLIENT_SECRET));
        return patientLoginAdapter.build().create(LoginAPI.class);
    }

    @Provides
    @Singleton
    OkClient provideOkClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final OkClient okClient = new OkClient(okHttpClient);
        // FIXME - move to okHttp 2.2+ to fix this
        // okClient.networkInterceptors().add(new StethoInterceptor());
        return okClient;
    }

    private RestAdapter.Builder restAdapterBuilder(final OkClient okClient,
                                                   final StringPreferenceEndpoint endpoint) {
        final Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT)
                .create();

        final RestAdapter.Builder commonBuilder = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setClient(okClient)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        return commonBuilder;
    }

    private RequestInterceptor authorizationInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(final RequestFacade request) {
                final String authorizationHeader = AUTHORIZATION_BEARER + " "
                        + Session.restore(mContext).getAccessToken();
                request.addHeader(AUTHORIZATION_HEADER, authorizationHeader);
            }
        };
    }

    private RequestInterceptor loginInterceptor(final String clientId,
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