package com.example.martin.movies.AsyncTasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;

import com.example.martin.movies.database.MoviesContract;

public class FetchMoviesDatabase implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int FETCH_DATABASE_MOVIES = 101;

    private final Context context;
    private final onFetchDatabaseComplete<Cursor> onComplete;
    private final LoaderManager manager;
    private final ContentResolver contentResolver;

    public FetchMoviesDatabase(Context context, onFetchDatabaseComplete<Cursor> onComplete, LoaderManager manager, ContentResolver resolver){
        this.context = context;
        this.contentResolver = resolver;
        this.manager = manager;
        this.onComplete = onComplete;
    }

    public void fetchMovies(){
        Loader loader = manager.getLoader(FETCH_DATABASE_MOVIES);
        if(loader == null){
            manager.initLoader(FETCH_DATABASE_MOVIES, null, this);
        }else {
            manager.restartLoader(FETCH_DATABASE_MOVIES, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(context) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return contentResolver.query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        onComplete.onTaskComplete(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        if(loader != null)
            manager.restartLoader(FETCH_DATABASE_MOVIES, null, this);
    }



    public interface onFetchDatabaseComplete<T>{
        void onTaskComplete(T result);
    }
}
