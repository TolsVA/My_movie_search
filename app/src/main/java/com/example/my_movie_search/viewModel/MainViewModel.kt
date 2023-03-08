package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Repository
import com.example.my_movie_search.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl,
) :
    ViewModel() {
    fun getLiveData() = liveDataToObserve
    fun getMovie() = getDataFromLocalSource()
    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            when ((1..3).random()) {
                1 -> liveDataToObserve.postValue(
                    AppState.Success(repositoryImpl.getMovieFromLocalStorage())
                )
                2 -> liveDataToObserve.postValue(
                    AppState.Error(Throwable())
                )
                3 -> liveDataToObserve.postValue(
                    AppState.Loading
                )
            }
        }.start()
    }
}
