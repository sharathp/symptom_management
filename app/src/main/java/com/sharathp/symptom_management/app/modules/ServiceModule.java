package com.sharathp.symptom_management.app.modules;

import com.sharathp.symptom_management.service.PatientService;

import dagger.Module;

@Module(library = true,
        injects = {
            PatientService.class
        },
        complete = false)
public class ServiceModule {
    // no beans for now
}
