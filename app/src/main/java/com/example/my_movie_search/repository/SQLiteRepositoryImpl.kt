package com.example.my_movie_search.repository

import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.MainActivity

class SQLiteRepositoryImpl(): SQLiteRepository {
//    private val sqLiteManager = App.sqLiteManager()

    override fun getMovieFromSQLite(
        filter: String,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
//            callback.onSuccess(sqLiteManager.getMoviesFromDb(filter))
        }
    }

    override fun getMovieFromSQLite(id: Long, callback: Callback<MutableList<Movie>>) {
        MainActivity.getHandler().post {
//            callback.onSuccess(sqLiteManager.getMoviesFromDb(id))
        }
    }

    override fun getMovieFromSQLite(
        movies: MutableList<Movie>,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
//            callback.onSuccess(sqLiteManager.getMoviesFromDb(movies))
        }
    }

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
//            sqLiteManager.insertMovieToDb(movies)
        }
    }

    override fun getMoviesPersonsIdFromSQLite(
        id: Long,
        callback: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
//            callback.onSuccess(sqLiteManager.getMoviesPersonsIdFromSQLite(id))
        }
    }
}