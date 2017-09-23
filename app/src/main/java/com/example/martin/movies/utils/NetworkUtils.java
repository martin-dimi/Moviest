package com.example.martin.movies.utils;

import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class NetworkUtils {

    private static final String CLASS_TAG = NetworkUtils.class.getName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String POSTER_BASE_ULR = "https://image.tmdb.org/t/p";
    private static final String API_QUERY = "api_key";
    private static final String TOP_RATED = "movie/top_rated";
    private static final String POPULAR = "movie/popular";
    private static final String POSTER_SIZE = "w185";
    private static final String BACKDROP_SIZE = "w342";
    private static final String MOVIE_PATH = "movie";
    private static final String TRAILERS_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";
    private static final String BACKDROP_PATH = "images";
    private static final String LANGUAGE_QUERY = "language";
    private static final String ENGLISH_LANGUAGE = "en-US";

    //Insert your KEY here
    private static final String API_KEY = "your key";

    public static URL buildMovieListURL(boolean sort){

        String sort_preference = sort ? POPULAR : TOP_RATED;

        Log.i(CLASS_TAG, "Building URL");
        Uri movieURI = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sort_preference)
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL movieURL = null;
        try {
            movieURL = new URL(movieURI.toString());
        } catch (MalformedURLException e) {
            Log.e(CLASS_TAG, e.getMessage());
        }

        Log.i(CLASS_TAG, "URL build");
        return movieURL;
    }

    public static URL buildPosterURL(String poster, boolean isPoster){

        String size = isPoster ? POSTER_SIZE : BACKDROP_SIZE;

        Uri poster_Uri = Uri.parse(POSTER_BASE_ULR).buildUpon()
                .appendEncodedPath(size)
                .appendEncodedPath(poster)
                .build();

        URL poster_Url = null;
        try {
            poster_Url = new URL(poster_Uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return poster_Url;
    }

    public static URL buildTrailersURL(int movieId){

        Uri trailersUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath("" + movieId)
                .appendPath(TRAILERS_PATH)
                .appendQueryParameter(API_QUERY, API_KEY)
                .appendQueryParameter(LANGUAGE_QUERY, ENGLISH_LANGUAGE)
                .build();

        URL trailersURL = null;
        try {
            trailersURL = new URL(trailersUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return trailersURL;
    }

    public static URL buildReviewsURL(int movieId){
        Uri trailersUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath("" + movieId)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL trailersURL = null;
        try {
            trailersURL = new URL(trailersUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return trailersURL;
    }

    public static URL buildBackDropURL(int movieId){
        Uri trailersUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath("" + movieId)
                .appendPath(BACKDROP_PATH)
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL trailersURL = null;
        try {
            trailersURL = new URL(trailersUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return trailersURL;
    }

    public static String getResponseFromURL(URL url){

        String result = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();

            result = IOUtils.toString(input, StandardCharsets.UTF_8);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
