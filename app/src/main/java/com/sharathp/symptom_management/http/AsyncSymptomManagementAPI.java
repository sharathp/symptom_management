package com.sharathp.symptom_management.http;

import com.sharathp.symptom_management.model.Doctor;
import com.sharathp.symptom_management.model.Medication;
import com.sharathp.symptom_management.model.Patient;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface AsyncSymptomManagementAPI {

    @GET("/doctors/{id}")
    void getDoctorById(@Path("id") String id, Callback<Doctor> callback);

    @GET("/patients/{id}")
    void getPatientById(@Path("id") String id, Callback<Patient> callback);

    @GET("/patients/{id}/medications")
    void getPatientMedications(@Path("id") String id, Callback<List<Medication>> callback);

    @PUT("/patients/{id}/medications")
    void updatePatientMedications(@Path("id") String id, @Body List<Medication> medications,
                                      Callback<Response> callback);

    @POST("/patients/{id}/check-ins")
    void addPatientCheckin(@Body PatientCheckIn checkIn, Callback<Response> callback);

    @GET("/patients/{id}/check-ins")
    void getPatientCheckins(@Query("from") Date from,
                                            Callback<List<PatientCheckIn>> callback);

    @GET("/patients")
    void getPatientsByName(@Query("name") String name, Callback<List<Patient>> callback);
}
