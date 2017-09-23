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

        URL backDropURL = NetworkUtils.buildBackDropURL(movieId);
        String backdrop = MovieUtils.getBackDrop(NetworkUtils.getResponseFromURL(backDropURL));

        List<String> data = MovieUtils.getTrailersIds(trailersJSON);
        data.add(backdrop);

        return data;
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
