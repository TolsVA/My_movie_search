package com.example.my_movie_search.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource

class DetailViewModel(
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource())
) : ViewModel() {
    fun getLiveDataDetail() = messageDetailFragment
}