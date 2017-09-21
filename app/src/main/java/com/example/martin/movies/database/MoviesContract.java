package com.example.martin.movies.database;


import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract{

    static final String AUTHORITY = "com.example.martin.movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_RELEASE_DATE = "release";
        static final String COLUMN_POSTER = "poster";
        static final String TABLE_NAME = "movies_favourite";

    }

}
