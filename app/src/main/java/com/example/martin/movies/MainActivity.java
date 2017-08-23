package com.example.martin.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martin.movies.AsyncTasks.AsyncTaskComplete;
import com.example.martin.movies.AsyncTasks.MovieQueryTask;
import com.example.martin.movies.Views.MovieAdapter;
import com.example.martin.movies.models.Movie;
import com.example.martin.movies.utils.MovieJSONUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener{

    private static final String CLASS_LOG = MainActivity.class.getName();

    @BindView(R.id.rv_movie_list) RecyclerView mMovieList;
    @BindView(R.id.pb_loading) ProgressBar progressBar;
    @BindView(R.id.tv_error_internet) TextView error_internet;
    MovieAdapter adapter;

    private List<Movie> movies;
    private boolean preference = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), numberOfColumns());
        mMovieList.setLayoutManager(layoutManager);
        mMovieList.setHasFixedSize(true);

        adapter = new MovieAdapter(this);
        mMovieList.setAdapter(adapter);

        fetchMovies();
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

    private void fetchMovies(){
        if(isOnline()) {
            hideErrorMessage();
            hideLoadingIndicator();
            new MovieQueryTask(this, new FetchMovieDataTaskComplete(), preference).execute();
        }
        else{
            showErrorMessage();
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 400;
        int screenWidth = displayMetrics.widthPixels;
        int nColumns = screenWidth / widthDivider;

        return (nColumns < 2) ? 2 : nColumns;
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

    @Override
    public void movieOnClick(int position) {
        Movie selected_movie = movies.get(position);
        Class destination = MovieDetails.class;
        Intent intent = new Intent(this, destination);
        intent.putExtra("Movie", selected_movie);
        startActivity(intent);
    }

    private class FetchMovieDataTaskComplete implements AsyncTaskComplete<String>{

        @Override
        public void onTaskComplete(String movie_json) {
            movies = MovieJSONUtils.getMoviesFromJSON(movie_json);
            assert movies != null;
            Log.i(CLASS_LOG, "Number of holders: " + Integer.toString(movies.size()));
            adapter.setMovieData(movies);
            showLoadingIndicator();
        }
    }
}
