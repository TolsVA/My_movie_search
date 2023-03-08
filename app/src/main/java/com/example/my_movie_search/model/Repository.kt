package com.example.my_movie_search.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): ArrayList<Movie>
}