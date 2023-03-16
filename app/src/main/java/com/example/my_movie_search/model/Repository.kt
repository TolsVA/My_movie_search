package com.example.my_movie_search.model

interface Repository {
    fun getMovieFromLocalStoragePortWorld(): MutableList<Movie>
    fun getMovieFromLocalStorageLandWorld(): MutableList<Movie>
    fun getMovieFromLocalStoragePortraitRus(): MutableList<Movie>
    fun getMovieFromLocalStorageLandscapeRus(): MutableList<Movie>
}