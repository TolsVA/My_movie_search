package com.example.my_movie_search.repository

import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId

interface MoviesRepository {
    fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>)
    fun getMovieFromNetServer(id: Long?, callback: retrofit2.Callback<MovieList>)
    fun getMoviePersonsFromNetServer(id: Long?, callback: retrofit2.Callback<MovieListPersonsId>)
    fun getMovieFromSQLite(filter: String, callback: Callback<MutableList<Movie>>)
    fun getMovieFromSQLite(id: Long?, callback: Callback<MutableList<Movie>>)
    fun insertMovieToDb(movies: MutableList<Movie>)
    fun getMoviesPersonsIdFromSQLite(id: Long?, callback: Callback<MutableList<Movie>>)

}