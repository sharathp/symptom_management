package com.sharathp.symptom_management.app;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation created to improve Context injection. This annotation is used with @Inject for
 * Context class to return the Application.
 */
@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}