package com.sharathp.symptom_management.http;

import com.sharathp.symptom_management.model.Doctor;
import com.sharathp.symptom_management.model.Medication;
import com.sharathp.symptom_management.model.Patient;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Date;
import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 *
 */
public interface SymptomManagementAPI {

    @GET("/doctors/{id}")
    Doctor getDoctorById(@Path("id") String id);

    @GET("/patients/{id}")
    Patient getPatientById(@Path("id") String id);

    @GET("/patients/{id}/medications")
    List<Medication> getPatientMedications(@Path("id") String id);

    @PUT("/patients/{id}/medications")
    Response updatePatientMedications(@Path("id") String id, @Body List<Medication> medications);

    @POST("/patients/{id}/check-ins")
    Response addPatientCheckin(@Body PatientCheckIn checkIn);

    @GET("/patients/{id}/check-ins")
    List<PatientCheckIn> getPatientCheckins(@Query("from") Date from);

    @GET("/patients")
    List<Patient> getPatientsByName(@Query("name") String name);
}
