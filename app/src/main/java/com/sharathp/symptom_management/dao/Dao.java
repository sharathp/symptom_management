package com.sharathp.symptom_management.dao;

import android.content.ContentValues;
import android.database.Cursor;

public interface Dao<T> {
    long insert(ContentValues values);

    Cursor query(String[] projection, String selection,
                 String[] selectionArgs, String sortOrder);

    Cursor queryById(long _id, String[] projection);

    int update(ContentValues values, String selection, String[] selectionArgs);

    int updateEntry(long _id, ContentValues values);

    int delete(String selection, String[] selectionArgs);
}
