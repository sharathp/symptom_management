package com.sharathp.symptom_management.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.dao.ReminderDao;

import java.util.List;

import javax.inject.Inject;

public class SymptomManagementProvider extends ContentProvider {
    private static final String TAG = SymptomManagementProvider.class.getSimpleName();

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
    DoctorDao mDoctorDao;

    @Inject
    PatientDao mPatientDao;

    @Inject
    MedicationDao mMedicationDao;

    @Inject
    ReminderDao mReminderDao;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SymptomManagementContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ReminderContract.PATH_REMINDER, REMINDER);
        matcher.addURI(authority, ReminderContract.PATH_REMINDER + "/#", REMINDER_ID);

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
    public String getType(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case REMINDER:
                return ReminderContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return ReminderContract.ReminderEntry.CONTENT_ITEM_TYPE;
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
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case REMINDER_ID:
                retCursor = mReminderDao.queryById(ContentUris.parseId(uri), projection);
                break;
            case REMINDER:
                retCursor = mReminderDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case PATIENT_ID:
                retCursor = mPatientDao.queryById(ContentUris.parseId(uri), projection);
                break;
            case PATIENT:
                retCursor = mPatientDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_ID:
                retCursor = mDoctorDao.queryById(ContentUris.parseId(uri), projection);
                break;
            case DOCTOR:
                retCursor = mDoctorDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_PATIENTS:
                retCursor = mPatientDao.getPatientsForDoctor(getDoctor_id(uri), projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        Uri returnUri = null;
        switch(sUriMatcher.match(uri)) {
            case PATIENT:
                final long patient_id = mPatientDao.insert(values);
                if ( patient_id > 0 ) {
                    returnUri = PatientContract.PatientEntry.buildPatientUri(patient_id);
                    notifyPatientsChanged(uri, values);
                }
                break;
            case DOCTOR:
                final long doctor_id = mDoctorDao.insert(values);
                if ( doctor_id > 0 ) {
                    returnUri = DoctorContract.DoctorEntry.buildDoctorUri(doctor_id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(returnUri != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.e(TAG, "Failed to insert row into " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case PATIENT:
                rowsDeleted = mPatientDao.delete(selection, selectionArgs);
                break;
            case DOCTOR:
                rowsDeleted = mDoctorDao.delete(selection, selectionArgs);
                break;
            case REMINDER:
                rowsDeleted = mReminderDao.delete(selection, selectionArgs);
                break;
            case MEDICATION:
                rowsDeleted = mMedicationDao.delete(selection, selectionArgs);
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
        int rowsUpdated = 0;

        switch (sUriMatcher.match(uri)) {
            case PATIENT:
                rowsUpdated = mPatientDao.update(values, selection, selectionArgs);
                break;
            case PATIENT_ID:
                rowsUpdated = mPatientDao.updateEntry(ContentUris.parseId(uri), values);
                break;
            case DOCTOR:
                rowsUpdated = mDoctorDao.update(values, selection, selectionArgs);
                break;
            case DOCTOR_ID:
                rowsUpdated = mDoctorDao.updateEntry(ContentUris.parseId(uri), values);
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

    private long getDoctor_id(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case DOCTOR_PATIENTS:
                final List<String> pathSegments = uri.getPathSegments();
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}