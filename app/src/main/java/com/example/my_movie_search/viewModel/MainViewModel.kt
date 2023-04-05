package com.example.my_movie_search.viewModel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.R
import com.example.my_movie_search.model.*
import com.google.gson.Gson

class MainViewModel(
    private val liveDataToObserveLocal: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserveNet: MutableLiveData<AppState> = MutableLiveData(),
    private val messageDetailFragment: MutableLiveData<Movie> = MutableLiveData()
) : ViewModel() {

    fun getLiveDataLocal() = liveDataToObserveLocal
    fun getLiveDataNet() = liveDataToObserveNet
    fun getLiveDataDetail() = messageDetailFragment
    fun getMovie(movies: MutableList<Movie>) = getDataFromLocalSource(movies)

    private fun getDataFromLocalSource(movies: MutableList<Movie>) {
        liveDataToObserveLocal.value = AppState.Loading
        liveDataToObserveLocal.value = AppState.Success(movies)
    }

    fun getLoadResultsReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getStringExtra(LOAD_RESULT_EXTRA)) {

                    REQUEST_ERROR_EXTRA -> {
                        liveDataToObserveNet.value = AppState.Error(
                            Throwable(intent.getStringExtra(REQUEST_ERROR_EXTRA))
                        )
                    }

                    RESPONSE_EMPTY -> {
                        liveDataToObserveNet.value = AppState.ResponseEmpty(
                                context.getString(R.string.response_empty)
                            )
                    }

                    RESPONSE_SUCCESS -> {
                        Gson().fromJson(
                            intent.getStringExtra(RESPONSE_SUCCESS),
                            MovieListNet::class.java
                        ).let {
                            liveDataToObserveNet.value = AppState.Success(it.docs)
                        }
                    }
                }
            }
        }
    }
}


