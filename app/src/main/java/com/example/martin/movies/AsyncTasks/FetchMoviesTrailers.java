package com.example.martin.movies.AsyncTasks;

import android.os.AsyncTask;

import com.example.martin.movies.utils.MovieUtils;
import com.example.martin.movies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class FetchMoviesTrailers extends AsyncTask<Integer, Void,  List<String> > {

    private final OnFetchTrailersComplete taskComplete;

    public FetchMoviesTrailers(OnFetchTrailersComplete taskComplete){
        this.taskComplete = taskComplete;
    }

    @Override
    protected List<String> doInBackground(Integer... integers) {
        int movieId = integers[0];

        URL trailersURL = NetworkUtils.buildTrailersURL(movieId);
        System.out.println(trailersURL);
        String trailersJSON = NetworkUtils.getResponseFromURL(trailersURL);

        return MovieUtils.getTrailersIds(trailersJSON);
    }

    @Override
    protected void onPostExecute(List<String> ids) {
        super.onPostExecute(ids);
        taskComplete.onTrailersFetchComplete(ids);
    }

    public interface OnFetchTrailersComplete{
        void onTrailersFetchComplete(List<String> data);
    }
}
