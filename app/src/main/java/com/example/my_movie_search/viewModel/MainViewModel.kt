package com.example.my_movie_search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.app.App.Companion.getAppDb
import com.example.my_movie_search.model.*
import com.example.my_movie_search.model.room.entity.MovieDbEntity
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.MoviesRoomRepository
import com.example.my_movie_search.repository.MoviesRoomRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
private const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class MainViewModel(
//    private var liveDataRoomMovie: MutableLiveData<MutableList<Movie>> = MutableLiveData(),
    private val liveDataToObserveNet: MutableLiveData<AppState> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource()),
    private val moviesRoomRepositoryImpl: MoviesRoomRepository = MoviesRoomRepositoryImpl(getAppDb())
) : ViewModel() {

    var filter: String = ""

    private var  liveDataRoomMovie: MutableLiveData<AppState> = moviesRoomRepositoryImpl.getAllMovie()
//    private var liveDataRoomMovie: MutableLiveData<List<Movie>> = MutableLiveData()

    fun getLiveDataNet() = liveDataToObserveNet

    fun getMovie(filter: String) = getDataFromNetSource(filter)
    fun getMovieRoom(filter: String) {
        this.filter = filter
        moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
    }

    fun getLiveDataRoomAll() = liveDataRoomMovie

    private fun getDataFromNetSource(filter: String) {
//        if (filter.isNotEmpty()) {this.filter = "*$filter*"}
        liveDataToObserveNet.value = AppState.Loading
        moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
    }



    private val callBack = object : retrofit2.Callback<MovieList> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()

            if (response.isSuccessful && serverResponse != null) {
//                if (serverResponse.movies.size > 0) {
//                    liveDataToObserveNet.postValue(AppState.Success(serverResponse.movies))
//                    moviesRepositoryImpl.insertMovieToDb(serverResponse.movies)
//                } else {
//                    liveDataToObserveNet.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
//                }

                moviesRoomRepositoryImpl.insertMovieToDb(serverResponse.movies)

//                Log.d("MyLog", "callBack serverResponse.movies -> ${serverResponse.movies}")
//                Thread {
//                    liveDataToObserveRoom = moviesRoomRepositoryImpl.getAllMovie(filter)
//                Log.d("MyLog", "callBack liveDataToObserveRoom.value -> ${liveDataToObserveRoom.value}")
//                }

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

//    private val callBackLocal = object : Callback<MutableList<Movie>> {
//        override fun onSuccess(result: MutableList<Movie>) {
//            if (result.size > 0) {
//                liveDataToObserveNet.postValue(AppState.Success(result))
//                if(result.size < 10) {
//                    moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
//                }
//            } else {
//                moviesRepositoryImpl.getMovieFromNetServer(filter, callBack)
//            }
//        }
//
//        override fun onError(error: Throwable?) {
//            TODO()
//        }
//    }
}