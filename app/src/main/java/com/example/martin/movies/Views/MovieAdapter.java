package com.example.martin.movies.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.martin.movies.R;
import com.example.martin.movies.models.Movie;
import com.example.martin.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String CLASS_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movies;
    private final MovieOnClickListener listener;

    public MovieAdapter(MovieOnClickListener listener){
        this.movies = new ArrayList<>();
        this.listener = listener;
    }

    public interface MovieOnClickListener{
        void movieOnClick(int position);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context parentContext = parent.getContext();
        int movieItemLayout = R.layout.rv_movie_item;

        View movieItem = LayoutInflater.from(parentContext).inflate(movieItemLayout, parent, false);

        return new MovieViewHolder(movieItem, listener, parentContext);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setMoviePoster(movies.get(position).getPosterURL());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView mMovieArt;
        private final Context parent;
        private final MovieOnClickListener listener;

        MovieViewHolder(View itemView, MovieOnClickListener listener, Context parent) {
            super(itemView);

            this.listener = listener;
            this.parent = parent;
            mMovieArt = itemView.findViewById(R.id.iv_movie_image);
            itemView.setOnClickListener(this);
        }

        void setMoviePoster(String posterPath){
            String posterURL = NetworkUtils.buildPosterURL(posterPath, true).toString();
            Log.i(CLASS_TAG, "Fetching the poster from" + posterURL);
            Picasso.with(parent)
                    .load(posterURL)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder_error)
                    .into(mMovieArt);
        }

        @Override
        public void onClick(View view) {
            Log.i(CLASS_TAG, "BUTTON PRESSED");
            int position = getAdapterPosition();
            listener.movieOnClick(position);
        }
    }
    public void setMovieData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
