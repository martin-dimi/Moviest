package com.example.martin.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private double rating;
    private String poster;
    private String overview;
    private String releaseDate;

    public Movie(){

    }

    public Movie(int id, String title, double rating, String poster, String overview, String releaseDate) {

        this.id = id;
        this.title = title;
        this.rating = rating;
        this.poster = poster;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.rating = in.readDouble();
        this.poster = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(rating);
        parcel.writeString(poster);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
