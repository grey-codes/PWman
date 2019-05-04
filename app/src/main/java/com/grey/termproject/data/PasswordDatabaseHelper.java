//PasswordDatabaseHelper.java

//change to PasswordDatabaseHelper.java

package com.grey.termproject.data;


import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.grey.termproject.data.DatabaseDescription.Password;

public class PasswordDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Password.db";
    private static final int DATABASE_VERSION = 1;

    public PasswordDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PASSWORD_TABLE =
                "CREATE TABLE " + DatabaseDescription.Password.TABLE_NAME +
                        "(" + Password._ID + " integer primary key, " +
                        Password.COLUMN_USERNAME + " TEXT, " +
                        DatabaseDescription.Password.COLUMN_PASSWORD + " TEXT, " +
                        DatabaseDescription.Password.COLUMN_ACCOUNT + " TEXT ";
        db.execSQL(CREATE_PASSWORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}