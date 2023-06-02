package com.example.my_movie_search.repository

import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies
import com.example.my_movie_search.repository.retrofit.RemoteDataSource

class RetrofitRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RetrofitRepository {

    override fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>) {
        remoteDataSource.getMovies(filter, callback)
    }

    override fun getMovieFromNetServer(movies: MutableList<Movies>, callback: retrofit2.Callback<MovieList>) {
        remoteDataSource.getMoviesID(movies, callback)
    }

    override fun getMoviePersonsIdFromNetServer(
        id: Long,
        callback: retrofit2.Callback<MovieListPersonsId>
    ) {
        remoteDataSource.getPersonsIdAPI(id, callback)
    }
}
