package com.example.martin.movies.AsyncTasks;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import com.example.martin.movies.utils.NetworkUtils;

import java.net.URL;

public class FetchMoviesNetwork implements LoaderManager.LoaderCallbacks<String> {

    private static final int FETCH_ONLINE_MOVIES = 100;
    private static final String PREFERENCE_ID = "preference_id";

    private final Context context;
    private final AsyncTaskComplete<String> onComplete;
    private final LoaderManager manager;

    public FetchMoviesNetwork(Context context, LoaderManager manager, AsyncTaskComplete<String> onComplete) {
        this.context = context;
        this.manager = manager;
        this.onComplete = onComplete;
    }

    public void fetchMovies(boolean preferences){
        Bundle bundle = new Bundle();
        bundle.putBoolean(PREFERENCE_ID, preferences);

        Loader loader = manager.getLoader(FETCH_ONLINE_MOVIES);
        if(loader == null){
            manager.initLoader(FETCH_ONLINE_MOVIES, bundle, this);
        }else {
            manager.restartLoader(FETCH_ONLINE_MOVIES, bundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(context) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                boolean preferences = bundle.getBoolean(PREFERENCE_ID);
                URL url = NetworkUtils.buildMovieListURL(preferences);
                return NetworkUtils.getResponseFromURL(url);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String movieJson) {
        onComplete.onTaskComplete(movieJson);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        if(loader != null)
             manager.restartLoader(FETCH_ONLINE_MOVIES, null, this);
    }

    public interface AsyncTaskComplete<T> {

        void onTaskComplete(T result);
    }
}