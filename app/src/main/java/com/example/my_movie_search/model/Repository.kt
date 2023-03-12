package com.example.my_movie_search.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStoragePortraitWorld(): MutableList<Movie>
    fun getMovieFromLocalStorageLandscapeWorld(): MutableList<Movie>
    fun getMovieFromLocalStoragePortraitRus(): MutableList<Movie>
    fun getMovieFromLocalStorageLandscapeRus(): MutableList<Movie>
}