package com.example.martin.movies.models;

import java.io.Serializable;


public class Movie implements Serializable {

    private int id;
    private String title;
    private double rating;
    private String poster;
    private String overview;
    private String release_date;

    public Movie(){

    }

    public Movie(int id, String title, double rating, String poster, String overview, String release_date) {

        this.id = id;
        this.title = title;
        this.rating = rating;
        this.poster = poster;
        this.overview = overview;
        this.release_date = release_date;
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

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


}
