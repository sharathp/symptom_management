package com.sharathp.symptom_management.app.modules;

import com.sharathp.symptom_management.service.DoctorService;
import com.sharathp.symptom_management.service.MedicationService;
import com.sharathp.symptom_management.service.PatientCheckInService;
import com.sharathp.symptom_management.service.PatientService;

import dagger.Module;

@Module(library = true,
        injects = {
            PatientService.class,
            DoctorService.class,
            MedicationService.class,
            PatientCheckInService.class
        },
        complete = false)
public class ServiceModule {
    // no beans for now
}
