package com.sharathp.symptom_management.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sharathp.symptom_management.app.SymptomManagementApplication;
import com.sharathp.symptom_management.dao.CheckinMedicationDao;
import com.sharathp.symptom_management.dao.DoctorDao;
import com.sharathp.symptom_management.dao.MedicationDao;
import com.sharathp.symptom_management.dao.PatientCheckInDao;
import com.sharathp.symptom_management.dao.PatientDao;
import com.sharathp.symptom_management.dao.ReminderDao;
import com.sharathp.symptom_management.data.provider.contract.DoctorContract;
import com.sharathp.symptom_management.data.provider.contract.MedicationContract;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
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

    private static final int PATIENT_PATIENT_CHECKINS = 600;
    private static final int PATIENT_CHECKINS = 601;
    private static final int PATIENT_CHECKIN_ID = 602;

    private static final int CHECKIN_MEDICATIONS = 700;

    private static final int RECENT_CHECKINS = 800;

    @Inject
    DoctorDao mDoctorDao;

    @Inject
    PatientDao mPatientDao;

    @Inject
    MedicationDao mMedicationDao;

    @Inject
    ReminderDao mReminderDao;

    @Inject
    PatientCheckInDao mPatientCheckInDao;

    @Inject
    CheckinMedicationDao mCheckinMedicationDao;

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

        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#/" + PatientCheckInContract.PATH_CHECKINS, PATIENT_PATIENT_CHECKINS);
        matcher.addURI(authority, PatientCheckInContract.PATH_CHECKINS, PATIENT_CHECKINS);
        matcher.addURI(authority, PatientCheckInContract.PATH_CHECKINS + "/#", PATIENT_CHECKIN_ID);

        matcher.addURI(authority, PatientCheckInContract.PATH_CHECKINS + "/#/" + PatientCheckInContract.PATH_CHECKIN_MEDICATIONS, CHECKIN_MEDICATIONS);

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
            case PATIENT_PATIENT_CHECKINS:
                return PatientCheckInContract.PatientCheckInEntry.CONTENT_TYPE;
            case PATIENT_CHECKIN_ID:
                return PatientCheckInContract.PatientCheckInEntry.CONTENT_ITEM_TYPE;
            case CHECKIN_MEDICATIONS:
                return PatientCheckInContract.PatientCheckInMedicationIntakeEntry.CONTENT_TYPE;
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
                retCursor = mReminderDao.queryById(getReminderId(uri), projection);
                break;
            case REMINDERS:
                retCursor = mReminderDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case PATIENT_ID:
                retCursor = mPatientDao.queryById(getPatientId(uri), projection);
                break;
            case PATIENTS:
                retCursor = mPatientDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_ID:
                retCursor = mDoctorDao.queryById(getDoctorId(uri), projection);
                break;
            case DOCTORS:
                retCursor = mDoctorDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case DOCTOR_PATIENTS:
                retCursor = mPatientDao.getPatientsForDoctor(getDoctorId(uri), projection, sortOrder);
                break;
            case MEDICATIONS:
                retCursor = mMedicationDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case MEDICATION_ID:
                retCursor = mMedicationDao.queryById(getMedicationId(uri), projection);
                break;
            case PATIENT_MEDICATIONS:
                retCursor = mPatientDao.getMedicationsForPatient(getPatientId(uri), projection, sortOrder);
                break;
            case PATIENT_PATIENT_CHECKINS:
                retCursor = mPatientCheckInDao.getPatientCheckIns(getPatientId(uri), projection, sortOrder);
                break;
            case PATIENT_CHECKINS:
                retCursor = mPatientCheckInDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case PATIENT_CHECKIN_ID:
                retCursor = mPatientCheckInDao.queryById(getPatientCheckinId(uri), projection);
                break;
            case CHECKIN_MEDICATIONS:
                retCursor = mCheckinMedicationDao.getAllMedicationsForCheckin(getPatientCheckinId(uri), projection, sortOrder);
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
            case PATIENTS:
                final long patientId = mPatientDao.insert(values);
                if ( patientId > 0 ) {
                    returnUri = PatientContract.PatientEntry.buildPatientUri(patientId);
                    notifyPatientsChanged(uri, values);
                }
                break;
            case DOCTORS:
                final long doctorId = mDoctorDao.insert(values);
                if ( doctorId > 0 ) {
                    returnUri = DoctorContract.DoctorEntry.buildDoctorUri(doctorId);
                }
                break;
            case MEDICATIONS:
                final long medicationId = mMedicationDao.insert(values);
                if ( medicationId > 0 ) {
                    returnUri = MedicationContract.MedicationEntry.buildMedicationUri(medicationId);
                }
                break;
            case PATIENT_MEDICATION_ID:
                mPatientDao.addMedicationForPatient(getPatientId(uri), getMedicationId(uri));
                returnUri = uri;
                break;
            case PATIENT_PATIENT_CHECKINS:
                final long patientCheckInId = mPatientCheckInDao.createPatientCheckIn(getPatientId(uri), values);
                if ( patientCheckInId > 0 ) {
                    returnUri = PatientCheckInContract.PatientCheckInEntry.buildPatientCheckInUri(patientCheckInId);
                }
                break;
            case CHECKIN_MEDICATIONS:
                final long checkinMedicationId = mCheckinMedicationDao.createCheckInMedication(getPatientCheckinId(uri), values);
                if ( checkinMedicationId > 0 ) {
                    returnUri = PatientCheckInContract.PatientCheckInMedicationIntakeEntry.buildCheckInMedicationUri(checkinMedicationId);
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
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)) {
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
                rowsDeleted = mPatientDao.deleteMedicationForPatient(getPatientId(uri), getMedicationId(uri));
                break;
            case PATIENT_PATIENT_CHECKINS:
                rowsDeleted = mPatientCheckInDao.deletePatientCheckIns(getPatientId(uri), selection, selectionArgs);
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
                rowsUpdated = mPatientDao.updateEntry(getPatientId(uri), values);
                break;
            case DOCTORS:
                rowsUpdated = mDoctorDao.update(values, selection, selectionArgs);
                break;
            case DOCTOR_ID:
                rowsUpdated = mDoctorDao.updateEntry(getDoctorId(uri), values);
                break;
            case MEDICATIONS:
                rowsUpdated = mMedicationDao.update(values, selection, selectionArgs);
                break;
            case MEDICATION_ID:
                rowsUpdated = mMedicationDao.updateEntry(getMedicationId(uri), values);
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

    private long getDoctorId(final Uri uri) {
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

    private long getPatientId(final Uri uri) {
        final List<String> pathSegments = uri.getPathSegments();
        switch (sUriMatcher.match(uri)) {
            case PATIENT_ID:
                return ContentUris.parseId(uri);
            case PATIENT_MEDICATIONS:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            case PATIENT_MEDICATION_ID:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 3));
            case PATIENT_PATIENT_CHECKINS:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getMedicationId(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MEDICATION_ID:
            case PATIENT_MEDICATION_ID:
                return ContentUris.parseId(uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getReminderId(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case REMINDER_ID:
                return ContentUris.parseId(uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getPatientCheckinId(final Uri uri) {
        final List<String> pathSegments = uri.getPathSegments();
        switch (sUriMatcher.match(uri)) {
            case PATIENT_CHECKIN_ID:
                return ContentUris.parseId(uri);
            case CHECKIN_MEDICATIONS:
                return Long.parseLong(pathSegments.get(pathSegments.size() - 2));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}