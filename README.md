# Movies App

## Description
Fully functional app for [Android Developer Nanodegree program](https://www.udacity.com/course/android-developer-nanodegree--nd801). This app uses responsive design for phones and tablets. It is reviewed by Udacity code reviewer. 

## Features
Android application designed to browse the most viewed/most popular movies, depending on the user. By clicking on a movie poster,
it leads to a Details screen, which shows Rating, Release date, Description, Trailer and User reviews for
the selected movie. You can also add movies to a favourites list by clicking on the favourites button,
for later offline display. Fetches movies data from movieDB.com.
With the app, you can:
* Discover the most popular or the highest rated movies
* Save favorite movies locally to view them even when offline
* Watch trailers
* Read reviews
* Share movies

## How to Work with the Source

This app uses [The Movie Database](https://www.themoviedb.org/documentation/api) API to retrieve movies.
You must provide your own API key in order to build the app. When you get it, just paste it to `network/MovieModule.java` file: 
```java    
public static final String API_KEY = "YOU_API_KEY";
```

## Screenshots
<img src="images/menu.png" height="444" width="250"> <img src="images/details1.png" height="444" width="250"> <img src="images/details2.png" height="444" width="250">
