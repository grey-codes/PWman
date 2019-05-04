//database description

// might need to change package names

package com.grey.termproject.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription
{
	public static final String AUTHORITY = "com.grey.termproject.data";
	
	private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final class Password implements BaseColumns
	{
		public static final String TABLE_NAME = "passwords";
		
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
		
		public static final String COLUMN_USERNAME = "username";
		public static final String COLUMN_PASSWORD = "password";
		public static final String COLUMN_ACCOUNT = "account";
		
		public static Uri buildPasswordUri(long id)
		{
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
