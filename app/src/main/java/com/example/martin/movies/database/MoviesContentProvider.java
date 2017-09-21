package com.example.martin.movies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.martin.movies.database.MoviesContract.*;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.*;


public class MoviesContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;

    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MoviesDBHelper mDbHelper;

    private static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, PATH_MOVIES, MOVIES);
        matcher.addURI(AUTHORITY, PATH_MOVIES + "/#", MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor data;

        switch (match){
            case MOVIES:
                data = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                data = database.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("URI not supported");
        }
        return data;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = database.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 )
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Couldn't insert movie");
                break;

            default:
                throw new UnsupportedOperationException("URI not supported");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int changed;

        switch(match){
            case MOVIES:
                changed = database.delete(TABLE_NAME, whereClause, whereArgs);
                break;
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                changed = database.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("URI not supported");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return changed;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
