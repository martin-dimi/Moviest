package com.example.martin.movies.AsyncTasks;

import android.os.AsyncTask;

import com.example.martin.movies.models.Review;
import com.example.martin.movies.utils.MovieUtils;
import com.example.martin.movies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;


public class FetchMoviesReviews extends AsyncTask<Integer, Void,  List<Review> > {

    private final OnFetchReviewsComplete taskComplete;

    public FetchMoviesReviews(OnFetchReviewsComplete taskComplete){
        this.taskComplete = taskComplete;
    }

    @Override
    protected List<Review> doInBackground(Integer... integers) {
        int movieId = integers[0];

        URL reviewsURL = NetworkUtils.buildReviewsURL(movieId);
        System.out.println(reviewsURL);
        String trailersJSON = NetworkUtils.getResponseFromURL(reviewsURL);

        return MovieUtils.getReviews(trailersJSON);
    }

    @Override
    protected void onPostExecute(List<Review> ids) {
        super.onPostExecute(ids);
        taskComplete.onReviewsFetchComplete(ids);
    }

    public interface OnFetchReviewsComplete{
        void onReviewsFetchComplete(List<Review> data);
    }
}