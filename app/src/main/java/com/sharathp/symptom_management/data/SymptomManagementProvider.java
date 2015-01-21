package com.sharathp.symptom_management.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sharathp.symptom_management.app.SymptomManagementApplication;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */
public class SymptomManagementProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int REMINDER = 100;
    private static final int REMINDER_ID = 101;

    private static final int PATIENT = 200;
    private static final int PATIENT_ID = 201;

    @Inject
    Lazy<SQLiteDatabase> database;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SymptomManagementContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER, REMINDER);
        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER + "/#", REMINDER_ID);

        matcher.addURI(authority, PatientContract.PATH_PATIENT, PATIENT);
        matcher.addURI(authority, PatientContract.PATH_PATIENT + "/#", PATIENT_ID);

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

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Override
    public String getType(final Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case REMINDER:
                return SymptomManagementContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return SymptomManagementContract.ReminderEntry.CONTENT_ITEM_TYPE;
            case PATIENT:
                return PatientContract.PatientEntry.CONTENT_TYPE;
            case PATIENT_ID:
                return PatientContract.PatientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match) {
            case PATIENT: {
                final long _id = database.get().insert(PatientContract.PatientEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PatientContract.PatientEntry.buildPatientUri(_id);
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
        return 0;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return 0;
    }
}
