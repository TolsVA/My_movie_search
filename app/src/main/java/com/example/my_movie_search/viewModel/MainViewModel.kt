package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Repository
import com.example.my_movie_search.model.RepositoryImpl

class MainViewModel(
    private val liveDataToObserveLocal: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserveNet: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl(),
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData()
) : ViewModel() {

    fun getLiveDataLocal() = liveDataToObserveLocal
    fun getLiveDataNet() = liveDataToObserveNet
    fun getLiveDataDetail() = messageDetailFragment
    fun getMovie( filter: String ) = getDataFromNetSource( filter )
    fun getMovie( movies: MutableList<Movie> ) = getDataFromLocalSource( movies )

    private fun getDataFromLocalSource(movies: MutableList<Movie>) {
        liveDataToObserveLocal.value = AppState.Loading
        liveDataToObserveLocal.value = AppState.Success(movies)
    }

    private fun getDataFromNetSource( filter: String ) {
        liveDataToObserveNet.value = AppState.Loading
        repositoryImpl.getMovieFromNetStorage(liveDataToObserveNet, filter)
    }
}
