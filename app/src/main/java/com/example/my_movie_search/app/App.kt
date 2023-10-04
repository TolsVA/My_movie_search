package com.example.my_movie_search.app

import android.app.Application
import androidx.room.Room
import com.example.my_movie_search.model.room.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
//        dbSQLite = sqLiteHelper.writableDatabase
    }

    companion object {
        lateinit var appInstance: App

//        private val sqLiteHelper: SQLiteHelper by lazy {
//            SQLiteHelper(appInstance.applicationContext)
//        }
//
//        private lateinit var dbSQLite: SQLiteDatabase
//
//        private val managerSQLite: SQLiteManager by lazy {
//            SQLiteManager(dbSQLite)
//        }
//
//        fun sqLiteManager() = managerSQLite

//        fun getDb() = dbSQLite

        private var db: AppDatabase? = null
        private const val DB_NAME = "room_database"

        fun getAppDb(): AppDatabase {
            return db ?: synchronized(AppDatabase::class.java) {
                val instance = Room.databaseBuilder(
                    appInstance.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                db = instance
                instance
            }
        }
    }
}