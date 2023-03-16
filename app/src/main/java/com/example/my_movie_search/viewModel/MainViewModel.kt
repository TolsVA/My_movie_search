package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Repository
import com.example.my_movie_search.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObservePortrait: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserveLandscape: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl,
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData()
) : ViewModel() {

    fun getLiveDataPortrait() = liveDataToObservePortrait
    fun getLiveDataLandscape() = liveDataToObserveLandscape
    fun getLiveDataDetail() = messageDetailFragment
    fun getMovie(isRus: Boolean) = getDataFromLocalSource(isRus)

    private fun getDataFromLocalSource(isRus: Boolean) {

        liveDataToObservePortrait.value = AppState.Loading
        liveDataToObserveLandscape.value = AppState.Loading

        Thread {
            sleep(1000)
            liveDataToObservePortrait.postValue(
                AppState.Success(
                    listMovies = when (isRus) {
                        true -> repositoryImpl.getMovieFromLocalStoragePortraitRus()
                        false -> repositoryImpl.getMovieFromLocalStoragePortWorld()
                    }
                )
            )
        }.start()

        Thread {
            sleep(1500)
            liveDataToObserveLandscape.postValue(
                AppState.Success(
                    listMovies = when (isRus) {
                        true -> repositoryImpl.getMovieFromLocalStorageLandscapeRus()
                        false -> repositoryImpl.getMovieFromLocalStorageLandWorld()
                    }
                )
            )
        }.start()
    }
}
