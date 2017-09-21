package com.example.martin.movies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.movies.AsyncTasks.FetchMoviesReviews;
import com.example.martin.movies.AsyncTasks.FetchMoviesTrailers;
import com.example.martin.movies.databinding.ActivityMovieDetailsBinding;
import com.example.martin.movies.models.Movie;
import com.example.martin.movies.models.Review;
import com.example.martin.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_OVERVIEW;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_RATING;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.COLUMN_TITLE;
import static com.example.martin.movies.database.MoviesContract.MoviesEntry.CONTENT_URI;

public class MovieDetails extends AppCompatActivity implements
        FetchMoviesTrailers.OnFetchTrailersComplete,
        FetchMoviesReviews.OnFetchReviewsComplete {

    private static final String CLASS_TAG = MovieDetails.class.getSimpleName();

    private Movie movieToDisplay;
    private ActivityMovieDetailsBinding primaryBinding;
    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        primaryBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        movieToDisplay = getIntent().getParcelableExtra("Movie");

        primaryBinding.tvMovieTitle.setText(movieToDisplay.getTitle());
        primaryBinding.tvMovieRelease.setText(movieToDisplay.getReleaseDate());
        primaryBinding.tvMovieRating.setText(String.valueOf(movieToDisplay.getRating()));
        primaryBinding.tvMovieDescription.setText(movieToDisplay.getOverview());

        URL posterURL = NetworkUtils.buildPosterURL(movieToDisplay.getPosterURL());
        Log.i(CLASS_TAG, "Fetching poster from: Memory");
        Picasso.with(this)
                .load(posterURL.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder_error)
                .into(primaryBinding.ivMoviePoster);


        new FetchMoviesTrailers(this).execute(movieToDisplay.getId());
        new FetchMoviesReviews(this).execute(movieToDisplay.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Cursor cursor = getContentResolver().query(CONTENT_URI.buildUpon().appendPath("" + movieToDisplay.getId()).build(), null, null, null);
            if (cursor.getCount() > 0) {
                setButtonToDelete();
            } else {
                setButtonToAdd();
            }
            cursor.close();
        }
    }

    public void onButtonPress(View view) {
        if (isFavourite)
            deleteMovie();
        else
            addMovie();
    }

    private void addMovie() {
        ContentResolver resolver = getContentResolver();
        ContentValues data = new ContentValues();

        data.put(_ID, movieToDisplay.getId());
        data.put(COLUMN_TITLE, movieToDisplay.getTitle());
        data.put(COLUMN_RATING, movieToDisplay.getRating());
        data.put(COLUMN_OVERVIEW, movieToDisplay.getOverview());

        Uri uri = resolver.insert(CONTENT_URI, data);
        if (uri != null) {
            Toast.makeText(this, getString(R.string.toast_add_movie), Toast.LENGTH_SHORT).show();
            setButtonToDelete();
        }
    }

    private void deleteMovie() {
        ContentResolver resolver = getContentResolver();
        int deleted = resolver.delete(CONTENT_URI.buildUpon().appendPath("" + movieToDisplay.getId()).build(), null, null);

        if (deleted > 0) {
            Toast.makeText(this, getString(R.string.toast_delete_movie), Toast.LENGTH_SHORT).show();
            setButtonToAdd();
        }
    }

    private void setButtonToAdd() {
        primaryBinding.bFavourite.setText(getString(R.string.button_fav));
        isFavourite = false;
    }

    private void setButtonToDelete() {
        primaryBinding.bFavourite.setText(getString(R.string.button_defav));
        isFavourite = true;
    }

    @Override
    public void onTrailersFetchComplete(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            final String id = data.get(i);

            View parent = LayoutInflater.from(this).inflate(R.layout.trailer_button, primaryBinding.constraint, false);

            Button trailer = parent.findViewById(R.id.b_trailer);
            trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent applicationIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube" + id));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + id));
                    try {
                        startActivity(applicationIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(browserIntent);
                    }
                }
            });

            if (trailer.getParent() != null)
                ((ViewGroup) trailer.getParent()).removeView(trailer);
            primaryBinding.lvTrailers.addView(trailer);
        }
    }

    @Override
    public void onReviewsFetchComplete(List<Review> data) {
        for (int i = 0; i < data.size(); i++) {
            final Review review = data.get(i);

            View parent = LayoutInflater.from(this).inflate(R.layout.review, primaryBinding.constraint, false);

            LinearLayout layout = parent.findViewById(R.id.review);
            TextView author = parent.findViewById(R.id.review_author);
            TextView content = parent.findViewById(R.id.review_content);

            author.setText(review.getAuthor());
            content.setText(review.getContent());

            if (layout.getParent() != null)
                ((ViewGroup) layout.getParent()).removeView(layout);
            primaryBinding.lvTrailers.addView(layout);
        }
    }
}