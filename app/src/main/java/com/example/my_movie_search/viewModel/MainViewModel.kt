package com.example.my_movie_search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.app.App.Companion.getAppDb
import com.example.my_movie_search.model.*
import com.example.my_movie_search.repository.Callback
import com.example.my_movie_search.repository.RetrofitRepository
import com.example.my_movie_search.repository.RetrofitRepositoryImpl
import com.example.my_movie_search.repository.RoomRepository
import com.example.my_movie_search.repository.RoomRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
private const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class MainViewModel(
    private var liveDataRoomFilterMovie: MutableLiveData<AppState> = MutableLiveData(),
    private var liveDataRetrofitMovie: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepImpl: RetrofitRepository = RetrofitRepositoryImpl(RemoteDataSource()),
    private val roomRepositoryImpl: RoomRepository = RoomRepositoryImpl(getAppDb()),
    private var liveDataRoomAllMovie: MutableLiveData<AppState> = roomRepositoryImpl.getAllMovie()
) : ViewModel() {
    var filter: String = "*"

    fun getMovieRoom(filter: String) {
        this.filter = if (filter == "") {
            liveDataRoomAllMovie.value = AppState.Loading
            "*"
        } else {
            liveDataRoomFilterMovie.value = AppState.Loading
            "*$filter*"
        }

        roomRepositoryImpl.getAllMovieFilter("*$filter*", callBackRoomMovieFilter)
//            if (listMovie.size > 0) {
//                liveDataRoomFilterMovie.postValue(
//                    AppState.Success(listMovie)
//                )
//            }


//        retrofitRepImpl.getMovieFromNetServer(filter, callBack)
    }

    private val callBackRoomMovieFilter = object : Callback<MutableList<Movie>> {
        override fun onSuccess(result: MutableList<Movie>) {
            if (result.isNotEmpty()) {
                liveDataRoomFilterMovie.postValue(AppState.Success(result))
            }
        }

        override fun onError(error: Throwable?) {

        }
    }

    fun getLiveDataRoomAll() = liveDataRoomAllMovie

    fun getLiveDataRoomFilterMovie() = liveDataRoomFilterMovie
    fun getLiveDataRetrofitMovie() = liveDataRetrofitMovie

    private val callBack = object : retrofit2.Callback<MovieList> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()

            if (response.isSuccessful && serverResponse?.movies != null) {
                val listMovie = mutableListOf<Movie>()
                serverResponse.movies.forEach {
                    if (it.name != null) {
                        listMovie.add(it)
                    }
                }
                roomRepositoryImpl.insertMovieToDb(listMovie)

//                MainActivity.getHandler().post {
//                    val listMovieRoom = roomRepositoryImpl.getAllMovieFilter(
//                        filter,
//                        callBackRoomMovieFilter
//                    )
//                    if (listMovieRoom.size > 0) {
//                        liveDataRoomFilterMovie.postValue(
//                            AppState.Success(listMovieRoom)
//                        )
//                    } else {
//                        liveDataRetrofitMovie.postValue(AppState.Error(Throwable(RESPONSE_EMPTY)))
//                    }
//                }
            } else if (!response.isSuccessful) {
                liveDataRetrofitMovie.postValue(AppState.Error(Throwable(RESPONSE_EMPTY)))
            } else {
                liveDataRetrofitMovie.postValue(AppState.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            liveDataRetrofitMovie.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }
}