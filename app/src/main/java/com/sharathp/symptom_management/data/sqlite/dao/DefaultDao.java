package com.sharathp.symptom_management.data.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharathp.symptom_management.dao.Dao;

import javax.inject.Inject;

public abstract class DefaultDao<T> implements Dao<T> {

    @Inject
    protected SQLiteDatabase mDatabase;
    protected String mTable;
    protected String m_idColumn = "_id";

    @Override
    public long insert(final ContentValues values) {
        return mDatabase.insert(mTable, null, values);
    }

    @Override
    public Cursor query(final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        return mDatabase.query(
                mTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor queryById(final long _id, final String[] projection) {
        return mDatabase.query(
                mTable,
                projection,
                m_idColumn + " = ?",
                new String[] {Long.toString(_id)},
                null,
                null,
                null
        );
    }

    @Override
    public int update(final ContentValues values, final String selection, final String[] selectionArgs) {
        return mDatabase.update(mTable, values, selection, selectionArgs);
    }

    @Override
    public int updateEntry(final long _id, final ContentValues values) {
        return mDatabase.update(mTable, values,
                m_idColumn + " = ?",
                new String[]{Long.toString(_id)});
    }

    @Override
    public int delete(final String selection, final String[] selectionArgs) {
        return mDatabase.delete(mTable, selection, selectionArgs);
    }

    public void setDatabase(final SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public void setTable(final String table) {
        this.mTable = table;
    }

    public void set_idColumn(final String _idColumn) {
        this.m_idColumn = _idColumn;
    }
}
