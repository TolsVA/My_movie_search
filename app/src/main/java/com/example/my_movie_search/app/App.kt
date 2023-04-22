package com.example.my_movie_search.app

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.example.my_movie_search.model.sqlite.SQLiteHelper
import com.example.my_movie_search.model.sqlite.SQLiteManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        db = sqLiteHelper.writableDatabase
    }

    companion object {
        lateinit var appInstance: App

        private val sqLiteHelper: SQLiteHelper by lazy {
            SQLiteHelper(appInstance.applicationContext)
        }

        private lateinit var db: SQLiteDatabase

        private val managerSQLite: SQLiteManager by lazy {
            SQLiteManager(db)
        }

        fun sqLiteManager() = managerSQLite

        fun getDb() = db
    }
}


