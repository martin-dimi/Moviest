package com.example.martin.movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.movies.models.Movie;
import com.example.martin.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class MovieDetails extends AppCompatActivity {

    private static final String CLASS_TAG = MovieDetails.class.getSimpleName();

    private Movie movieToDisplay;
    @BindView(R.id.tv_movie_title) TextView title;
    @BindView(R.id.tv_movie_release_date) TextView releaseDate;
    @BindView(R.id.tv_movie_user_rating) TextView userRating;
    @BindView(R.id.tv_movie_description) TextView description;
    @BindView(R.id.iv_movie_poster) ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        movieToDisplay = (Movie) getIntent().getParcelableExtra("Movie");
        title.append(movieToDisplay.getTitle());
        releaseDate.append(movieToDisplay.getReleaseDate());
        userRating.append(Double.toString(movieToDisplay.getRating()));
        description.append(movieToDisplay.getOverview());

        URL posterURL = NetworkUtils.buildPosterURL(movieToDisplay.getPoster());
        Log.i(CLASS_TAG, "Fetching poster from: Memory");
        Picasso.with(this)
                .load(posterURL.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder_error)
                .into(poster);
    }
}
