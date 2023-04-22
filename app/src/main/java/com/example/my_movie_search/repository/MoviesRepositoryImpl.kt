package com.example.my_movie_search.repository

import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.view.MainActivity

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    private val sqLiteManager = App.sqLiteManager()

    override fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>) {
        remoteDataSource.getMovies(filter, callback)
    }

    override fun getMovieFromNetServer(id: Long, callback: retrofit2.Callback<MovieList>) {
//        remoteDataSource.getPersonsIdAPI(id, callback)
    }

    override fun getMovieFromSQLite(
        filter: String,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesFromDb(filter))
        }
    }

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
            sqLiteManager.insertMovieToDb(movies)
        }
    }

    override fun getMoviesPersonsFromSQLite(
        id: Long?,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesPersonsFromSQLite(id))
        }
    }
}
