//PasswordContentProvider.java

//might need to change package names
//might need to change lines 16 and 17

package com.grey.termporject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.grey.passwords.R;
import com.grey.termproject.DatabaseDescription.Password;


public class PasswordContentProvider extends ContentProvider
{
    private PasswordDatabaseHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_PASSWORD = 1;
    private static final int PASSWORDS = 2;

    static 
	{
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Password.TABLE_NAME + "/#", ONE_PASSWORD);

        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Password.TABLE_NAME, PASSWORDS);
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new PasswordDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder)
    {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Password.TABLE_NAME);

        switch (uriMatcher.match(uri))
        {
            case ONE_PASSWORD:
                queryBuilder.appendWhere(
                        DatabaseDescription.Password._ID + "=" + uri.getLastPathSegment());
                break;
            case PASSWORDS:
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Uri newPasswordUri = null;

        switch (uriMatcher.match(uri))
        {
            case PASSWORDS:
                long rowId = dbHelper.getWritableDatabase().insert(
                        Password.TABLE_NAME, null, values);

                if (rowId > 0)
                {
                    newPasswordUri = DatabaseDescription.Password.buildPasswordUri(rowId);

                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newRecipeUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int numberOfRowsUpdated;

        switch (uriMatcher.match(uri))
        {
            case ONE_PASSWORD:
                String id = uri.getLastPathSegment();

                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Password.TABLE_NAME, values, Password._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }

        if (numberOfRowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri))
        {
            case ONE_PASSWORD:
                String id = uri.getLastPathSegment();

                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        DatabaseDescription.Password.TABLE_NAME,
                        Pasword._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        if (numberOfRowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
