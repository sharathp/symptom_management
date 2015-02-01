package com.sharathp.symptom_management.data.provider;

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
import com.sharathp.symptom_management.data.provider.contract.DoctorContract;
import com.sharathp.symptom_management.data.provider.contract.MedicationContract;
import com.sharathp.symptom_management.data.provider.contract.PatientContract;
import com.sharathp.symptom_management.data.provider.contract.ReminderContract;
import com.sharathp.symptom_management.data.provider.contract.SymptomManagementContract;

import java.util.List;

import javax.inject.Inject;

public class SymptomManagementProvider extends ContentProvider {
    private static final String TAG = SymptomManagementProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int REMINDERS = 100;
    private static final int REMINDER_ID = 101;

    private static final int PATIENTS = 200;
    private static final int PATIENT_ID = 201;

    private static final int DOCTORS = 300;
    private static final int DOCTOR_ID = 301;
    private static final int DOCTOR_PATIENTS = 302;

    private static final int MEDICATIONS = 400;
    private static final int MEDICATION_ID = 401;

    private static final int PATIENT_MEDICATIONS = 500;
    private static final int PATIENT_MEDICATION_ID = 501;

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

        matcher.addURI(authority, ReminderContract.PATH_REMINDER, REMINDERS);
        matcher.addURI(authority, ReminderContract.PATH_REMINDER + "/#", REMINDER_ID);

        matcher.addURI(authority, PatientContract.PATH_PATIENT, PATIENTS);
        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#", PATIENT_ID);

        matcher.addURI(authority, DoctorContract.PATH_DOCTOR, DOCTORS);
        matcher.addURI(authority, DoctorContract.PATH_DOCTOR + "/#", DOCTOR_ID);
        matcher.addURI(authority, DoctorContract.PATH_DOCTOR + "/#/" + PatientContract.PATH_PATIENT, DOCTOR_PATIENTS);

        matcher.addURI(authority, MedicationContract.PATH_MEDICATION, MEDICATIONS);
        matcher.addURI(authority, MedicationContract.PATH_MEDICATION + "/#", MEDICATION_ID);

        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#/" + MedicationContract.PATH_MEDICATION, PATIENT_MEDICATIONS);
        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#/" + MedicationContract.PATH_MEDICATION + "/#", PATIENT_MEDICATION_ID);

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
            case REMINDERS:
                return ReminderContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return ReminderContract.ReminderEntry.CONTENT_ITEM_TYPE;
            case PATIENTS:
                return PatientContract.PatientEntry.CONTENT_TYPE;
            case PATIENT_ID:
                return PatientContract.PatientEntry.CONTENT_ITEM_TYPE;
            case DOCTORS:
                return DoctorContract.DoctorEntry.CONTENT_TYPE;
            case DOCTOR_ID:
                return DoctorContract.DoctorEntry.CONTENT_ITEM_TYPE;
            case DOCTOR_PATIENTS:
                return PatientContract.PatientEntry.CONTENT_TYPE;
            case PATIENT_MEDICATIONS:
                return MedicationContract.MedicationEntry.CONTENT_TYPE;
            case PATIENT_MEDICATION_ID:
                return MedicationContract.MedicationEntry.CONTENT_ITEM_TYPE;
            case MEDICATIONS:
                return MedicationContract.MedicationEntry.CONTENT_TYPE;
            case MEDICATION_ID:
                return MedicationContract.MedicationEntry.CONTENT_ITEM_TYPE;
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
                retCursor = mReminderDao.queryById(getReminder_Id(uri), projection);
                break;
            case REMINDERS:
                retCursor = mReminderDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case PATIENT_ID:
                retCursor = mPatientDao.queryById(getPatient_Id(uri), projection);
                break;
            case PATIENTS:
                retCursor = mPatientDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_ID:
                retCursor = mDoctorDao.queryById(getDoctor_id(uri), projection);
                break;
            case DOCTORS:
                retCursor = mDoctorDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_PATIENTS:
                retCursor = mPatientDao.getPatientsForDoctor(getDoctor_id(uri), projection, sortOrder);
                break;
            case MEDICATIONS:
                retCursor = mMedicationDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case MEDICATION_ID:
                retCursor = mMedicationDao.queryById(getMedication_Id(uri), projection);
                break;
            case PATIENT_MEDICATIONS:
                retCursor = mPatientDao.getMedicationsForPatient(getPatient_Id(uri), projection, sortOrder);
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
        long patient_id = 0;
        long medication_id = 0;
        long doctor_id = 0;

        switch(sUriMatcher.match(uri)) {
            case PATIENTS:
                patient_id = mPatientDao.insert(values);
                if ( patient_id > 0 ) {
                    returnUri = PatientContract.PatientEntry.buildPatientUri(patient_id);
                    notifyPatientsChanged(uri, values);
                }
                break;
            case DOCTORS:
                doctor_id = mDoctorDao.insert(values);
                if ( doctor_id > 0 ) {
                    returnUri = DoctorContract.DoctorEntry.buildDoctorUri(doctor_id);
                }
                break;
            case MEDICATIONS:
                medication_id = mMedicationDao.insert(values);
                if ( medication_id > 0 ) {
                    returnUri = MedicationContract.MedicationEntry.buildMedicationUri(medication_id);
                }
                break;
            case PATIENT_MEDICATION_ID:
                patient_id = getPatient_Id(uri);
                medication_id = getMedication_Id(uri);
                mPatientDao.addMedicationForPatient(patient_id, medication_id);
                returnUri = uri;
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
        long patient_id = 0;
        long medication_id = 0;
        int rowsDeleted = 0;
        switch (match) {
            case PATIENTS:
                rowsDeleted = mPatientDao.delete(selection, selectionArgs);
                break;
            case DOCTORS:
                rowsDeleted = mDoctorDao.delete(selection, selectionArgs);
                break;
            case REMINDERS:
                rowsDeleted = mReminderDao.delete(selection, selectionArgs);
                break;
            case MEDICATIONS:
                rowsDeleted = mMedicationDao.delete(selection, selectionArgs);
                break;
            case PATIENT_MEDICATION_ID:
                patient_id = getPatient_Id(uri);
                medication_id = getMedication_Id(uri);
                rowsDeleted = mPatientDao.deleteMedicationForPatient(patient_id, medication_id);
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
            case PATIENTS:
                rowsUpdated = mPatientDao.update(values, selection, selectionArgs);
                break;
            case PATIENT_ID:
                rowsUpdated = mPatientDao.updateEntry(getPatient_Id(uri), values);
                break;
            case DOCTORS:
                rowsUpdated = mDoctorDao.update(values, selection, selectionArgs);
                break;
            case DOCTOR_ID:
                rowsUpdated = mDoctorDao.updateEntry(getDoctor_id(uri), values);
                break;
            case MEDICATIONS:
                rowsUpdated = mMedicationDao.update(values, selection, selectionArgs);
                break;
            case MEDICATION_ID:
                rowsUpdated = mMedicationDao.updateEntry(getMedication_Id(uri), values);
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
            case PATIENTS:
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
        final List<String> pathSegments = uri.getPathSegments();
        switch (sUriMatcher.match(uri)) {
            case DOCTOR_ID:
                return ContentUris.parseId(uri);
            case DOCTOR_PATIENTS:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getPatient_Id(final Uri uri) {
        final List<String> pathSegments = uri.getPathSegments();
        switch (sUriMatcher.match(uri)) {
            case PATIENT_ID:
                return ContentUris.parseId(uri);
            case PATIENT_MEDICATIONS:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            case PATIENT_MEDICATION_ID:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 3));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getMedication_Id(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MEDICATION_ID:
            case PATIENT_MEDICATION_ID:
                return ContentUris.parseId(uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getReminder_Id(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case REMINDER_ID:
                return ContentUris.parseId(uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}