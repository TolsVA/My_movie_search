package com.example.my_movie_search.model

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.my_movie_search.BuildConfig
import com.example.my_movie_search.viewModel.AppState
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class RepositoryImpl : Repository {
    override fun getMovieFromNetStorage(
        liveDataToObserveNet: MutableLiveData<AppState>,
        filter: String
    ) {
        loadMovie( liveDataToObserveNet, filter )
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie(
        liveDataToObserveNet: MutableLiveData<AppState>,
        filter: String
    ) = try {
        val url = URL(
            "https://api.kinopoisk.dev/v1/movie?name=$filter"
        )

        val handler = Handler(Looper.getMainLooper())

        Thread {
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                    addRequestProperty("x-api-key", BuildConfig.MOVIE_API_KEY)
                }

                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLines(reader)

                val movieListNet : MovieListNet = Gson().fromJson(result, MovieListNet::class.java)
                handler.post {
                    liveDataToObserveNet.postValue(
                        AppState.Success(
                            listMovies =  movieListNet.docs
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    liveDataToObserveNet.postValue(
                        AppState.Error(error = Throwable())
                    )
                }
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    } catch (e: MalformedURLException) {
        e.printStackTrace()
        liveDataToObserveNet.postValue(
            AppState.Error(error = Throwable())
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(
            Collectors.joining("\n")
        )
    }
}