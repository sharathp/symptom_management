package com.sharathp.symptom_management.app.qualifiers;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
// Qualifier to be used in lieu with StringPreference to annotate the
// endpoint url, useful for development..
public @interface ApiEndpoint {

}
