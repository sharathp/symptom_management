package com.sharathp.symptom_management.data;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.sharathp.symptom_management.app.SymptomManagementApplication;

/**
 *
 */
public class SymptomManagementProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int REMINDER = 100;
    private static final int REMINDER_ID = 101;

    private SQLiteDatabase database;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SymptomManagementContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER, REMINDER);
        matcher.addURI(authority, SymptomManagementContract.PATH_REMINDER + "/#", REMINDER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        database = SymptomManagementApplication.getInstance().getDb();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case REMINDER_ID: {
                retCursor = database.query(
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
            // "location"
            case REMINDER: {
                retCursor = database.query(
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

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case REMINDER:
                return SymptomManagementContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return SymptomManagementContract.ReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
