@file:Suppress("DEPRECATION")

package com.example.my_movie_search.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import javax.net.ssl.HttpsURLConnection
import com.example.my_movie_search.BuildConfig
import org.json.JSONObject
import java.net.URL
import java.util.stream.Collectors

const val FILTER_EXTRA = "FILTER_EXTRA"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "x-api-key"

const val LOAD_RESULT_EXTRA = "LOAD_RESULT_EXTRA"
const val RESPONSE_SUCCESS = "RESPONSE_SUCCESS"
const val RESPONSE_EMPTY = "RESPONSE_EMPTY"
const val INTENT_FILTER = "INTENT_FILTER"
const val REQUEST_ERROR_EXTRA = "REQUEST_ERROR_EXTRA"

class MovieIntentService(name: String = "MovieService") : IntentService(name) {

    private val broadcastIntent = Intent(INTENT_FILTER)

    @SuppressLint("ObsoleteSdkInt")
    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val filter = intent.getStringExtra(FILTER_EXTRA)
            loadMovies(filter)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovies(filter: String?) {
        try {
            val url = URL(
                "https://api.kinopoisk.dev/v1/movie?name=$filter"
            )
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(
                        REQUEST_API_KEY,
                        BuildConfig.MOVIE_API_KEY
                    )
                }

                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLines(reader)

                onResponse(result)

            } catch (e: Exception) {
                onErrorRequest(e.javaClass.name.toString())
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL(e.javaClass.name.toString())
        }
    }

    private fun onMalformedURL(error: String) {
        onErrorRequest(error)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(REQUEST_ERROR_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponse(result: String) {
        if (JSONObject(result).getJSONArray("docs").length() > 0) {
            onSuccessResponse(result)
        } else {
            onEmptyResponse()
        }
    }

    private fun onEmptyIntent() {
        onErrorRequest("java.lang.NullPointerException")
    }

    private fun onEmptyResponse() {
        putLoadResult(RESPONSE_EMPTY)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onSuccessResponse(result: String) {
        putLoadResult(RESPONSE_SUCCESS)
        broadcastIntent.putExtra (RESPONSE_SUCCESS, result)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(LOAD_RESULT_EXTRA, result)
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(
            Collectors.joining("\n")
        )
    }
}