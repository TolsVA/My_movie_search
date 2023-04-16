package com.example.my_movie_search.repository

import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList

interface MoviesRepository {
    fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>)
    fun getMovieFromNetServer(id: Long, callback: retrofit2.Callback<MovieList>)
    fun getMovieFromSQLite(filter: String, callback: Callback<MutableList<Movie>>)
    fun insertMovieToDb(movies: MutableList<Movie>)
    fun getMoviesPersonsFromSQLite(id: Long?, callback: Callback<MutableList<Movie>>)

}