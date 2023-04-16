package com.example.my_movie_search.app

import android.app.Application

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
//            SQLiteManager(db.writableDatabase)
//        }
//
//        fun getSQLiteDb(): SQLiteManager = managerSQLite
    }
}


