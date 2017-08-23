package com.example.martin.movies.AsyncTasks;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.martin.movies.utils.MovieJSONUtils;
import com.example.martin.movies.utils.NetworkUtils;

import java.net.URL;

public class MovieQueryTask extends AsyncTask<String, Void, String> {

    private Context context;
    private AsyncTaskComplete<String> onComplete;
    private boolean preferences;

    public MovieQueryTask(Context context, AsyncTaskComplete<String> onComplete, boolean preferences) {
        this.context = context;
        this.onComplete = onComplete;
        this.preferences = preferences;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = NetworkUtils.buildMovieListURL(preferences);
        return NetworkUtils.getResponseFromURL(url);
    }

    @Override
    protected void onPostExecute(String movie_json) {
        onComplete.onTaskComplete(movie_json);
    }
}