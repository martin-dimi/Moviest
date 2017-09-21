package com.example.martin.movies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.martin.movies.database.MoviesContract.MoviesEntry;

class MoviesDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 6;

    MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_RATING + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT" +
                MoviesEntry.COLUMN_POSTER + "BLOB" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
