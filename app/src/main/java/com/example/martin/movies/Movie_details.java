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

@SuppressWarnings("ALL")
public class Movie_details extends AppCompatActivity {

    private static final String CLASS_TAG = Movie_details.class.getSimpleName();

    private Movie movieToDisplay;
    private TextView title;
    private TextView release_date;
    private TextView user_rating;
    private TextView description;
    private ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        title = (TextView) findViewById(R.id.tv_movie_title);
        release_date = (TextView) findViewById(R.id.tv_movie_release_date);
        user_rating = (TextView) findViewById(R.id.tv_movie_user_rating);
        description = (TextView) findViewById(R.id.tv_movie_description);
        poster = (ImageView) findViewById(R.id.iv_movie_poster);

        movieToDisplay = (Movie) getIntent().getSerializableExtra("Movie");
        title.append(movieToDisplay.getTitle());
        release_date.append(movieToDisplay.getRelease_date());
        user_rating.append(Double.toString(movieToDisplay.getRating()));
        description.append(movieToDisplay.getOverview());


        URL posterURL = NetworkUtils.buildPosterURL(movieToDisplay.getPoster());
        Log.i(CLASS_TAG, "Fetching poster from: Memory");
        Picasso.with(this).load(posterURL.toString()).into(poster);
    }
}
