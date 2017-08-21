package com.example.martin.movies.utils;

import android.util.Log;

import com.example.martin.movies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJSONUtils {

    private static final String CLASS_TAG = MovieJSONUtils.class.getName();

    private static final String MOVIES = "results";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RATING = "vote_average";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "total_results";

    public static List<Movie> getMoviesFromJSON(String JSON){

        List<Movie> movies = null;
        Log.i(CLASS_TAG, "starting deserialize-ing");
        try {
            //getting the initial page
            JSONObject page = new JSONObject(JSON);

            //Whether the json file is supported or not
            if(!page.has(RESULTS)){
                Log.e(CLASS_TAG, "UNSUPPORTED JSON FILE");
                return null;
            }
            Log.i(CLASS_TAG, "Error check passed");

            //getting the list of movies inside the page
            JSONArray movieList = page.getJSONArray(MOVIES);
            int num_movies = movieList.length();
            movies = new ArrayList<>();

            //Going through the movies one by one
            Log.i(CLASS_TAG, "Starting JSONArray loop");
            for(int num = 0; num < num_movies; num++){
                JSONObject movie_JSON = movieList.getJSONObject(num);
                Log.i(CLASS_TAG, "Array loop number: " + num);

                //Getting the data for a movie
                int id = movie_JSON.getInt(ID);
                String title = movie_JSON.getString(TITLE);
                double rating = movie_JSON.getDouble(RATING);
                String poster = movie_JSON.getString(POSTER);
                String overview = movie_JSON.getString(OVERVIEW);
                String release_date = movie_JSON.getString(RELEASE_DATE);
                //

                Log.i(CLASS_TAG, "Adding movie: " + title);
                movies.add(new Movie(id, title, rating, poster, overview, release_date));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
