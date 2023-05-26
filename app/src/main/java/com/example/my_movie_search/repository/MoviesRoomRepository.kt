package com.example.my_movie_search.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.room.entity.MovieDbEntity
import com.example.my_movie_search.model.room.entity.PersonsDbEntity
import com.example.my_movie_search.viewModel.AppState


interface MoviesRoomRepository {
    fun getMovieFromRoom(filter: String, callBackLocal: Callback<MutableList<Movie>>)

    fun insertMovieToDb(movies: MutableList<Movie>)

    fun insertEntity(movies: MutableList<Movie>)

//    fun getAllMovie(): LiveData<List<MovieDbEntity>>
//    fun getAllMovie(): MutableLiveData<List<Movie>>

    fun findByIdMoviePersons(idRow: Long): MutableList<PersonsDbEntity>
    fun getAllMovie(): MutableLiveData<AppState>



}