package com.sharathp.symptom_management.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sharathp.symptom_management.app.SymptomManagementApplication;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class SymptomManagementProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int REMINDER = 100;
    private static final int REMINDER_ID = 101;

    private static final int PATIENT = 200;
    private static final int PATIENT_ID = 201;

    private static final int DOCTOR = 300;
    private static final int DOCTOR_ID = 301;
    private static final int DOCTOR_PATIENTS = 302;

    private static final int MEDICATION = 400;
    private static final int MEDICATION_ID = 401;

    @Inject
    Lazy<SQLiteDatabase> database;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SymptomManagementContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER, REMINDER);
        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER + "/#", REMINDER_ID);

        matcher.addURI(authority, PatientContract.PATH_PATIENT, PATIENT);
        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#", PATIENT_ID);

        matcher.addURI(authority, DoctorContract.PATH_DOCTOR, DOCTOR);
        matcher.addURI(authority, DoctorContract.PATH_DOCTOR + "/#", DOCTOR_ID);
        matcher.addURI(authority, DoctorContract.PATH_DOCTOR + "/#/patients", DOCTOR_PATIENTS);

        matcher.addURI(authority, MedicationContract.PATH_MEDICATION, MEDICATION);
        matcher.addURI(authority, MedicationContract.PATH_MEDICATION + "/#", MEDICATION_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        ((SymptomManagementApplication)getContext().getApplicationContext()).inject(this);
        return true;
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case REMINDER_ID: {
                retCursor = database.get().query(
                        SymptomManagementContract.ReminderEntry.TABLE_NAME,
                        projection,
                        SymptomManagementContract.ReminderEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REMINDER: {
                retCursor = database.get().query(
                        SymptomManagementContract.ReminderEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PATIENT_ID: {
                retCursor = database.get().query(
                        PatientContract.PatientEntry.TABLE_NAME,
                        projection,
                        PatientContract.PatientEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PATIENT: {
                retCursor = database.get().query(
                        PatientContract.PatientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DOCTOR_ID: {
                retCursor = database.get().query(
                        DoctorContract.DoctorEntry.TABLE_NAME,
                        projection,
                        DoctorContract.DoctorEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DOCTOR: {
                retCursor = database.get().query(
                        DoctorContract.DoctorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DOCTOR_PATIENTS: {
                final String doctor_id = getDoctor_id(uri);
                retCursor = database.get().query(
                        PatientContract.PatientEntry.TABLE_NAME,
                        projection,
                        PatientContract.PatientEntry.COLUMN_DOCTOR_ID + " = ?",
                        new String[] {doctor_id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case REMINDER:
                return SymptomManagementContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return SymptomManagementContract.ReminderEntry.CONTENT_ITEM_TYPE;
            case PATIENT:
                return PatientContract.PatientEntry.CONTENT_TYPE;
            case PATIENT_ID:
                return PatientContract.PatientEntry.CONTENT_ITEM_TYPE;
            case DOCTOR:
                return DoctorContract.DoctorEntry.CONTENT_TYPE;
            case DOCTOR_ID:
                return DoctorContract.DoctorEntry.CONTENT_ITEM_TYPE;
            case DOCTOR_PATIENTS:
                return PatientContract.PatientEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        Uri returnUri;
        switch(sUriMatcher.match(uri)) {
            case PATIENT: {
                final long _id = database.get().insert(PatientContract.PatientEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PatientContract.PatientEntry.buildPatientUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                final Uri doctorPatientsUri =  DoctorContract.DoctorEntry.buildPatientsUri(
                        values.getAsLong(PatientContract.PatientEntry.COLUMN_DOCTOR_ID));
                // notify patients changed for doctor..
                getContext().getContentResolver().notifyChange(doctorPatientsUri, null);
                break;
            }
            case DOCTOR: {
                final long _id = database.get().insert(DoctorContract.DoctorEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DoctorContract.DoctorEntry.buildDoctorUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case PATIENT:
                rowsDeleted = database.get().delete(
                        PatientContract.PatientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DOCTOR:
                rowsDeleted = database.get().delete(
                        DoctorContract.DoctorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case PATIENT:
                rowsUpdated = database.get().update(PatientContract.PatientEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PATIENT_ID:
                final long patient_id = ContentUris.parseId(uri);
                rowsUpdated = database.get().update(PatientContract.PatientEntry.TABLE_NAME, values,
                        PatientContract.PatientEntry._ID + " = ?", new String[]{Long.toString(patient_id)});
                break;
            case DOCTOR:
                rowsUpdated = database.get().update(DoctorContract.DoctorEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case DOCTOR_ID:
                final long doctor_id = ContentUris.parseId(uri);
                rowsUpdated = database.get().update(DoctorContract.DoctorEntry.TABLE_NAME, values,
                        DoctorContract.DoctorEntry._ID + " = ?", new String[]{Long.toString(doctor_id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // try best to notify changes..
    private void notifyPatientsChanged(final Uri uri, final ContentValues values) {
        Long doctor_id = null;

        switch (sUriMatcher.match(uri)) {
            case PATIENT:
            case PATIENT_ID:
                doctor_id = values.getAsLong(PatientContract.PatientEntry.COLUMN_DOCTOR_ID);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(doctor_id == null) {
            return;
        }

        final Uri doctorPatientsUri =  DoctorContract.DoctorEntry.buildPatientsUri(doctor_id);
        // notify patients changed for doctor..
        getContext().getContentResolver().notifyChange(doctorPatientsUri, null);
    }

    private String getDoctor_id(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case DOCTOR_PATIENTS:
                final List<String> pathSegments = uri.getPathSegments();
                return pathSegments.get(pathSegments.size() - 2);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}