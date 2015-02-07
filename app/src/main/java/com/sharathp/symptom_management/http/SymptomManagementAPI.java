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

    @GET("/api/v1/doctors/{id}")
    Doctor getDoctorByServerId(@Path("id") String id);

    @GET("/api/v1/doctors/{id}/patients")
    List<Patient> getPatientsForDoctor(@Path("id") String id);

    @GET("/api/v1/patients/{id}")
    Patient getPatientById(@Path("id") String id);

    @GET("/api/v1/patients/{id}/medications")
    List<Medication> getPatientMedications(@Path("id") String id);

    @PUT("/api/v1/patients/{id}/medications")
    Response updatePatientMedications(@Path("id") String id, @Body List<Medication> medications);

    @POST("/api/v1/patients/{id}/check-ins")
    Response addPatientCheckin(@Path("id") String id, @Body PatientCheckIn checkIn);

    @GET("/api/v1/patients/{id}/check-ins")
    List<PatientCheckIn> getPatientCheckins(@Path("id") String id, @Query("from") Date from);

    @GET("/api/v1/patients")
    List<Patient> getPatientsByName(@Query("name") String name);
}
