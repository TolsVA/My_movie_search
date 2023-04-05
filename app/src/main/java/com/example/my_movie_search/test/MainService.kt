@file:Suppress("DEPRECATION")

package com.example.my_movie_search.test

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private const val TAG = "MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
const val MAIN_SERVICE_INT_EXTRA = "MainServiceIntExtra"

class MainService(name: String = "MainService") : IntentService(name) {

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            sendBack(it.getStringExtra(MAIN_SERVICE_INT_EXTRA).toString())
        }
    }

    //Отправка уведомления о завершении сервиса
    private fun sendBack(result: String) {
        val broadcastIntent = Intent(TEST_BROADCAST_INTENT_FILTER)
        broadcastIntent.putExtra(THREADS_FRAGMENT_BROADCAST_EXTRA, result)
//        sendBroadcast(broadcastIntent)
        LocalBroadcastManager
            .getInstance( this )
            .sendBroadcast( broadcastIntent )
    }

    @Deprecated("Deprecated in Java")
    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }

    @Deprecated("Deprecated in Java")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    @Deprecated("Deprecated in Java")
    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }
}

