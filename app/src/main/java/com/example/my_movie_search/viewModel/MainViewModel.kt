package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.*
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.RemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class MainViewModel(
    private val liveDataToObserveLocal: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserveNet: MutableLiveData<AppState> = MutableLiveData(),
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveDataLocal() = liveDataToObserveLocal
    fun getLiveDataNet() = liveDataToObserveNet
    fun getLiveDataDetail() = messageDetailFragment
    fun getMovie(movies: MutableList<Movie>) = getDataFromLocalSource(movies)
    fun getMovie(filter: String) = getDataFromNetSource(filter)

    private fun getDataFromLocalSource(movies: MutableList<Movie>) {
        liveDataToObserveLocal.value = AppState.Loading
        liveDataToObserveLocal.value = AppState.Success(movies)
    }

    private fun getDataFromNetSource(filter: String) {
        liveDataToObserveNet.value = AppState.Loading
        moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
    }

    private val callBack = object : Callback<MovieListNet> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieListNet>, response: Response<MovieListNet>) {
            val serverResponse: MovieListNet? = response.body()

            if (response.isSuccessful && serverResponse != null) {
                liveDataToObserveNet.postValue(
                    if (serverResponse.docs.size > 0) {
                        AppState.Success(serverResponse.docs)
                    } else {
                        AppState.ResponseEmpty(RESPONSE_EMPTY)
                    }
                )
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieListNet>, t: Throwable) {
            liveDataToObserveNet.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }
}


