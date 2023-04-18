package com.example.my_movie_search.repository

import android.os.Handler
import android.os.HandlerThread
import com.example.my_movie_search.R
import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.view.MainActivity
import java.util.concurrent.Executor

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override fun getMovieFromNetServer(filter: String, callback: retrofit2.Callback<MovieList>) {
        remoteDataSource.getMovies(filter, callback)
    }

    override fun getMovieFromNetServer(id: Long, callback: retrofit2.Callback<MovieList>) {
        remoteDataSource.getPersonsIdAPI(id, callback)
    }

    override fun getMovieFromSQLite(
        filter: String,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            val result = MainActivity.sqLiteManager().getMoviesFromDb(filter)
            callback.onSuccess(result)
        }
    }

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
            MainActivity.sqLiteManager().insertMovieToDb(movies)
        }
    }

    override fun getMoviesPersonsFromSQLite(
        id: Long?,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            val result = MainActivity.sqLiteManager().getMoviesPersonsFromSQLite(id)
            callback.onSuccess(result)
        }
    }
}
