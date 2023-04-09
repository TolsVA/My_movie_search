package com.example.my_movie_search.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_movie_search.BuildConfig
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

private const val REQUEST_API_KEY = "x-api-key"
private const val MAIN_LINK = "https://api.kinopoisk.dev/v1/movie?"

class MovieService : Service() {
    var mBinder: IBinder? = null // Интерфейс связи с клиентом
    private val broadcastIntent = Intent(INTENT_FILTER)

    override fun onCreate() {
        // Создание службы
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        // Служба стартовала
        if (intent == null) {
//            onEmptyIntent()
        } else {
            val filter = intent.getStringExtra(FILTER_EXTRA)
            loadMovies(filter)
        }
        return START_NOT_STICKY
    }

    private fun loadMovies(filter: String?) {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.header(REQUEST_API_KEY, BuildConfig.MOVIE_API_KEY) // Создаём заголовок запроса
        builder.url( MAIN_LINK + "name=$filter") // Формируем URL

        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем

        call.enqueue(object : Callback {

            val handler: Handler = Handler(Looper.getMainLooper())

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body?.string() // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponse != null) {
                    handler.post {
                        inFullResponse(serverResponse)
                    }
                } else {
                    TODO("Not yet implemented")
                }
            }
        })
    }

    private fun inFullResponse(result: String) {
        if (JSONObject(result).getJSONArray("docs").length() > 0) {
            onSuccessResponse(result)
        } else {
            onEmptyResponse()
        }
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


    override fun onBind(intent: Intent?): IBinder? {
        // Привязка клиента
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // Удаление привязки
        return true
    }

    override fun onRebind(intent: Intent?) {
        // Перепривязка клиента
    }

    override fun onDestroy() {
        // Уничтожение службы
    }
}