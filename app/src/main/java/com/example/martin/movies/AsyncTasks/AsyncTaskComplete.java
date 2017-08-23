package com.example.martin.movies.AsyncTasks;


public interface AsyncTaskComplete<T> {

    public void onTaskComplete(T result);
}
