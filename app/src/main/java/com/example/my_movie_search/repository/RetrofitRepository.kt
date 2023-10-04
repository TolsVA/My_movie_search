package com.example.my_movie_search.repository

import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies

interface RetrofitRepository {
    fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>)
    fun getMovieFromNetServer(movies: MutableList<Movies>, callback: retrofit2.Callback<MovieList>)
    fun getMoviePersonsIdFromNetServer(id: Long, callback: retrofit2.Callback<MovieListPersonsId>)
}