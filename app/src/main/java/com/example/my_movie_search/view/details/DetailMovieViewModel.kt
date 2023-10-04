package com.example.my_movie_search.view.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Movie

class DetailMovieViewModel(
//    private val liveDataPersons: MutableLiveData<Movie> = MutableLiveData(),
//    private val roomRepositoryImpl: RoomRepository = RoomRepositoryImpl(App.getAppDb()),
) : ViewModel() {

     private var liveDataRoomMovie: LiveData<Movie> = MutableLiveData()
//
//
//    fun getLiveDataDetail() = liveDataRoomMovie
//    fun setMovie(movie: Movie) {
//        liveDataRoomMovie = roomRepositoryImpl.getMovie(movie.id!!)
//    }
}