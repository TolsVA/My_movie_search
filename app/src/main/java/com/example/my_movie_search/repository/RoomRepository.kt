package com.example.my_movie_search.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Genres
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.viewModel.AppState


interface RoomRepository {
    fun insertMovieToDb(movies: MutableList<Movie>)
    fun getAllMovie(): MutableLiveData<AppState>
    fun getAllMovieFilter(
        filter: String,
        callBackRoomMovieFilter: Callback<MutableList<Movie>>
    )
    fun findByIdMoviePersons(idRow: Long): List<Persons>
    fun findByIdMovieCountry(idRow: Long): List<Country>
    fun findByIdMovieGenres(idRow: Long): List<Genres>
    fun getMoviesPersonsId(personId: Long, callBackLocalIdPersons: Callback<List<Movie>>)
    fun getMovie(id: Long): LiveData<Movie>
}