package com.example.martin.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martin.movies.Views.MovieAdapter;
import com.example.martin.movies.models.Movie;
import com.example.martin.movies.utils.MovieJSONUtils;
import com.example.martin.movies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener{

    private static final String CLASS_LOG = MainActivity.class.getName();

    private RecyclerView mMovieList;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private TextView error_internet;

    private List<Movie> movies;
    private boolean preference = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        error_internet = (TextView) findViewById(R.id.tv_error_internet);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMovieList.setLayoutManager(layoutManager);
        mMovieList.setHasFixedSize(true);

        adapter = new MovieAdapter(this);
        mMovieList.setAdapter(adapter);

        fetchMovies();
    }

    private void fetchMovies(){
        if(isOnline()) {
            hideErrorMessage();
            new MovieQueryTask().execute();
        }
        else{
            showErrorMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int pressed_id = item.getItemId();
        int sort_button = R.id.ml_sort_button;
        int refresh_button = R.id.ml_refresh_button;

        if(pressed_id == sort_button){
            preference = !preference;
            fetchMovies();
            return true;
        }

        if(pressed_id == refresh_button){
            fetchMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMovies(){
        adapter.setMovieData(movies);
    }

    @Override
    public void movieOnClick(int position) {
        Movie selected_movie = movies.get(position);
        Class destination = Movie_details.class;
        Intent intent = new Intent(this, destination);
        intent.putExtra("Movie", selected_movie);
        startActivity(intent);
    }

    private class MovieQueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            hideLoadingIndicator();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url = NetworkUtils.buildMovieListURL(preference);
            return NetworkUtils.getResponseFromURL(url);
        }

        @Override
        protected void onPostExecute(String movie_json) {
            movies = MovieJSONUtils.getMoviesFromJSON(movie_json);
            assert movies != null;
            Log.i(CLASS_LOG, "Number of holders: " + Integer.toString(movies.size()));
            setMovies();
            showLoadingIndicator();
        }
    }

    private void showLoadingIndicator(){
        mMovieList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingIndicator(){
        mMovieList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(){
        mMovieList.setVisibility(View.INVISIBLE);
        error_internet.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage(){
        mMovieList.setVisibility(View.VISIBLE);
        error_internet.setVisibility(View.INVISIBLE);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
