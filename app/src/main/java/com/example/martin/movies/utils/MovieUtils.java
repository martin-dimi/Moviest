package com.example.martin.movies.utils;

import android.database.Cursor;
import android.util.Log;

import com.example.martin.movies.models.Movie;
import com.example.martin.movies.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_OVERVIEW;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_RATING;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_TITLE;

public class MovieUtils {

    private static final String CLASS_TAG = MovieUtils.class.getName();

    private static final String MOVIES = "results";
    private static final String BACKDROPS = "backdrops";
    private static final String BC_PATH = "file_path";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RATING = "vote_average";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "total_results";
    private static final String TRAILER_ID = "key";
    private static final String TRAILER_TYPE = "type";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";



    public static List<Movie> getMoviesFromJSON(String json){

        List<Movie> movies = new ArrayList<>();
        Log.i(CLASS_TAG, "starting deserialize-ing");
        try {
            //getting the initial page
            JSONObject page = new JSONObject(json);

            //Whether the json file is supported or not
            if(!page.has(RESULTS)){
                Log.e(CLASS_TAG, "UNSUPPORTED JSON FILE");
                return null;
            }
            Log.i(CLASS_TAG, "Error check passed");

            //getting the list of movies inside the page
            JSONArray movieList = page.getJSONArray(MOVIES);
            int num_movies = movieList.length();


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

    public static List<Movie> getMoviesFromCursor(Cursor cursor){
        int size = cursor.getCount();
        List<Movie> movies = new ArrayList<>();

        int id = cursor.getColumnIndex(_ID);
        int title = cursor.getColumnIndex(COLUMN_TITLE);
        int overview = cursor.getColumnIndex(COLUMN_OVERVIEW);
        int review = cursor.getColumnIndex(COLUMN_RATING);


        for(int i=0;i<size;i++){
            cursor.moveToPosition(i);
            Movie movie = new Movie();

            movie.setId(cursor.getInt(id));
            movie.setTitle(cursor.getString(title));
            movie.setOverview(cursor.getString(overview));
            movie.setRating(cursor.getDouble(review));

            movies.add(movie);
        }

        return movies;
    }

    public static List<String> getTrailersIds(String json){

        List<String> ids = new ArrayList<>();

        try {
            //getting the initial page
            JSONObject page = new JSONObject(json);

            JSONArray trailers = page.getJSONArray(MOVIES);
            int numTrailers= trailers.length();

            for(int i=0; i<numTrailers; i++){
                JSONObject trailer = trailers.getJSONObject(i);
                String type = trailer.getString(TRAILER_TYPE);
                if(type.equals("Trailer") || type.equals("Clip")) {
                    String trailerID = trailer.getString(TRAILER_ID);
                    ids.add(trailerID);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static List<Review> getReviews(String json){
        List<Review> reviews = new ArrayList<>();

        try {
            //getting the initial page
            JSONObject page = new JSONObject(json);

            JSONArray trailers = page.getJSONArray(MOVIES);
            int numReviews= trailers.length();

            for(int i=0; i<numReviews; i++){
                JSONObject reviewJson = trailers.getJSONObject(i);
                String author = reviewJson.getString(REVIEW_AUTHOR);
                String content = reviewJson.getString(REVIEW_CONTENT);
                Review review = new Review(author, content);
                reviews.add(review);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static String getBackDrop(String json){
        try {
            //getting the initial page
            JSONObject page = new JSONObject(json);

            JSONArray trailers = page.getJSONArray(BACKDROPS);

            JSONObject reviewJson = trailers.getJSONObject(0);
            return reviewJson.getString(BC_PATH);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
