package com.example.my_movie_search.repository

import com.example.my_movie_search.model.MovieListNet
import retrofit2.Callback


class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {
    override fun getMovieFromNetServer(filter: String, callback: Callback<MovieListNet>) {
        remoteDataSource.getMovies(filter, callback)
    }
}