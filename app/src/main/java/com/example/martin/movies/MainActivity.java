package com.example.martin.movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
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

import com.example.martin.movies.AsyncTasks.FetchMoviesDatabase;
import com.example.martin.movies.AsyncTasks.FetchMoviesNetwork;
import com.example.martin.movies.Views.MovieAdapter;
import com.example.martin.movies.models.Movie;
import com.example.martin.movies.utils.MovieUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("CanBeFinal")
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener{

    private static final String CLASS_LOG = MainActivity.class.getName();
    private static final String MOVIE_LIST = "movie_list";
    private static final String LIST_POSITION_KEY = "list_position";

    @BindView(R.id.rv_movie_list)  RecyclerView mMovieList;
    @BindView(R.id.pb_loading)  ProgressBar progressBar;
    @BindView(R.id.tv_error_internet)  TextView error_internet;
    private MovieAdapter adapter;
    private int adapterPosition;

    private List<Movie> movies;
    private boolean preference = false;
    private FetchMoviesNetwork fetchOnline;
    private FetchMoviesDatabase fetchDatabase;

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

        fetchOnline = new FetchMoviesNetwork(this, getLoaderManager(), new FetchMoviesOnlineTask());
        fetchDatabase = new FetchMoviesDatabase(this, new FetchMoviesDatabaseTask(), getLoaderManager(), getContentResolver());

        if(savedInstanceState != null){
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            adapterPosition = savedInstanceState.getInt(LIST_POSITION_KEY);
            adapter.setMovieData(movies);
            mMovieList.getLayoutManager().scrollToPosition(adapterPosition);
        }else {
            fetchMovies();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMovieList.getLayoutManager().scrollToPosition(adapterPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapterPosition = ((GridLayoutManager)mMovieList.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(LIST_POSITION_KEY, adapterPosition);
        outState.putParcelableArrayList(MOVIE_LIST, (ArrayList<? extends Parcelable>) movies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
        adapterPosition = savedInstanceState.getInt(LIST_POSITION_KEY);
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
        int favourites_button = R.id.ml_favourites_button;

        if(pressed_id == sort_button){
            preference = !preference;
            fetchMovies();
            return true;
        }

        else if(pressed_id == refresh_button){
            fetchMovies();
            return true;
        }

        else if(pressed_id == favourites_button){
            showFavourites();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFavourites() {
        hideErrorMessage();
        hideLoadingIndicator();
        fetchDatabase.fetchMovies();
    }

    private void fetchMovies(){
        if(isOnline()) {
            hideErrorMessage();
            hideLoadingIndicator();
            fetchOnline.fetchMovies(preference);
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

    private class FetchMoviesOnlineTask implements FetchMoviesNetwork.AsyncTaskComplete<String>{

        @Override
        public void onTaskComplete(String movie_json) {
            movies = MovieUtils.getMoviesFromJSON(movie_json);
            assert movies != null;
            Log.i(CLASS_LOG, "Number of holders: " + Integer.toString(movies.size()));
            adapter.setMovieData(movies);
            showLoadingIndicator();
        }
    }

    private class FetchMoviesDatabaseTask implements FetchMoviesDatabase.onFetchDatabaseComplete<Cursor>{

        @Override
        public void onTaskComplete(Cursor result) {
            movies = MovieUtils.getMoviesFromCursor(result);
            assert movies != null;
            adapter.setMovieData(movies);
            showLoadingIndicator();
        }
    }
}
