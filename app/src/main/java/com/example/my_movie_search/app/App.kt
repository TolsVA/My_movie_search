package com.example.my_movie_search.app

import android.app.Application
import com.example.my_movie_search.model.sqlite.SQLiteHelper
import com.example.my_movie_search.model.sqlite.SQLiteManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        lateinit var appInstance: App
//        private val db: SQLiteHelper by lazy {
//            SQLiteHelper(appInstance.applicationContext)
//        }
//
//        private val managerSQLite: SQLiteManager by lazy {
//            SQLiteManager(db.use { it.writableDatabase })
//        }
//
//        fun getSQLiteDb(): SQLiteManager = managerSQLite
    }
}


