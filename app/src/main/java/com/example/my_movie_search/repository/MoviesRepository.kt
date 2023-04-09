package com.example.my_movie_search.repository

import com.example.my_movie_search.model.MovieListNet
import retrofit2.Callback

interface MoviesRepository {
    fun getMovieFromNetServer(filter: String, callback: Callback<MovieListNet>)
}