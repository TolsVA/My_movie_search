package com.example.my_movie_search.repository

import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.view.MainActivity

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    private val sqLiteManager = App.sqLiteManager()

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

    override fun getMovieFromSQLite(
        filter: String,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesFromDb(filter))
        }
    }

    override fun getMovieFromSQLite(id: Long, callback: Callback<MutableList<Movie>>) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesFromDb(id))
        }
    }

    override fun getMovieFromSQLite(
        movies: MutableList<Movie>,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesFromDb(movies))
        }
    }

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
            sqLiteManager.insertMovieToDb(movies)
        }
    }

    override fun getMoviesPersonsIdFromSQLite(
        id: Long,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callback.onSuccess(sqLiteManager.getMoviesPersonsIdFromSQLite(id))
        }
    }
}
