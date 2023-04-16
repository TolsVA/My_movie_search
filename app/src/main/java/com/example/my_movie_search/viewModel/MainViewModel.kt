package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.*
import com.example.my_movie_search.repository.Callback
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class MainViewModel(
    private val liveDataToObserveLocal: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserveNet: MutableLiveData<AppState> = MutableLiveData(),
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData(),
    private val messageMoviesPersonsFragment: MutableLiveData<AppState> = MutableLiveData(),
    private val messageDetailPersonsFragment: MutableLiveData<Persons> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    var filter: String = ""
    var id: Long? = null
    fun getLiveDataNet() = liveDataToObserveNet
    fun getLiveDataDetail() = messageDetailFragment
    fun getMovie(filter: String) = getDataFromNetSource(filter)
    fun getLiveDataDetailPersons() = messageDetailPersonsFragment
    fun getLiveDataMoviesPersons() = messageMoviesPersonsFragment
    fun getPersonsMovie(persons: Persons?) {
        id = persons?.id
        messageMoviesPersonsFragment.value = AppState.Loading
//        moviesRepositoryImpl.getMoviesPersonsFromSQLite(id, callBackMPLocal)
    }


    private fun getDataFromNetSource(filter: String) {
        this.filter = filter
        liveDataToObserveNet.value = AppState.Loading
        moviesRepositoryImpl.getMovieFromSQLite(filter, callBackLocal)
    }


    private val callBack = object : retrofit2.Callback<MovieList> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()

            if (response.isSuccessful && serverResponse != null) {
                if (serverResponse.movies.size > 0) {
                    liveDataToObserveNet.postValue(AppState.Success(serverResponse.movies))
                    moviesRepositoryImpl.insertMovieToDb(serverResponse.movies)
                } else {
                    liveDataToObserveNet.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
                }
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            liveDataToObserveNet.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }

    private val callBackLocal = object : Callback<MutableList<Movie>> {
        override fun onSuccess(result: MutableList<Movie>) {
            if (result.size > 0) {
                liveDataToObserveNet.postValue(AppState.Success(result))
                if (result.size < 10) {
                    moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
                }
            } else {
                getDataFromNetSource(filter)
            }
        }

        override fun onError(error: Throwable?) {

        }
    }

    private val callBackMPLocal = object : Callback<MutableList<Persons>> {
        override fun onSuccess(result: MutableList<Persons>) {
            messageMoviesPersonsFragment.postValue(AppState.SuccessPersons(result))
            moviesRepositoryImpl.getMovieFromNetServer(id!!, callBack)
        }

        override fun onError(error: Throwable?) {

        }
    }
}


