package com.example.my_movie_search.repository

import androidx.lifecycle.LiveData
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.room.entity.MovieDbEntity
import com.example.my_movie_search.model.room.entity.PersonsDbEntity


interface MoviesRoomRepository {
    fun getMovieFromRoom(filter: String, callBackLocal: Callback<MutableList<Movie>>)

    fun insertMovieToDb(movies: MutableList<Movie>)

    fun insertEntity(movies: MutableList<Movie>)

    fun getAllMovie(filter: String): LiveData<List<MovieDbEntity>>

    fun findByIdMoviePersons(idRow: Long): MutableList<PersonsDbEntity>

}