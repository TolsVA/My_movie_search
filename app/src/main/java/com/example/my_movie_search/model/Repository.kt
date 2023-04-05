package com.example.my_movie_search.model

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_search.viewModel.AppState

interface Repository {
    fun getMovieFromNetStorage(liveDataToObserveNet: MutableLiveData<AppState>, filter: String)
}