package com.example.my_movie_search.repository

import com.example.my_movie_search.model.Movie

interface SQLiteRepository {
    fun getMoviesPersonsIdFromSQLite(id: Long, callback: Callback<MutableList<Movie>>)

    fun getMovieFromSQLite(filter: String, callback: Callback<MutableList<Movie>>)
    fun getMovieFromSQLite(id: Long, callback: Callback<MutableList<Movie>>)
    fun getMovieFromSQLite(movies: MutableList<Movie>, callback: Callback<MutableList<Movie>>)

    fun insertMovieToDb(movies: MutableList<Movie>)
}