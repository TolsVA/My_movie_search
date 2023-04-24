package com.example.my_movie_search.repository

import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies

interface MoviesRepository {
    fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>) // MainViewModel
    fun getMovieFromNetServer(id: Long, callback: retrofit2.Callback<MovieList>) // DetailPersonsViewModel

    fun getMoviePersonsIdFromNetServer(id: Long, callback: retrofit2.Callback<MovieListPersonsId>) // DetailPersonsViewModel
    fun getMoviesPersonsIdFromSQLite(id: Long, callback: Callback<MutableList<Movie>>) // DetailPersonsViewModel

    fun getMovieFromSQLite(filter: String, callback: Callback<MutableList<Movie>>) // MainViewModel
    fun getMovieFromSQLite(id: Long, callback: Callback<MutableList<Movie>>)
    fun getMovieFromSQLite(movies: MutableList<Movies>, callback: Callback<MutableList<Movie>>) // DetailPersonsViewModel

    fun insertMovieToDb(movies: MutableList<Movie>)  // MainViewModel, DetailPersonsViewModel
}