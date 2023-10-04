package com.example.my_movie_search.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_movie_search.model.room.dao.CountryDao
import com.example.my_movie_search.model.room.dao.GenresDao
import com.example.my_movie_search.model.room.dao.InsertMovieDao
import com.example.my_movie_search.model.room.dao.MovieCountrySettingsDao
import com.example.my_movie_search.model.room.dao.MovieDao
import com.example.my_movie_search.model.room.dao.MovieGenresSettingsDao
import com.example.my_movie_search.model.room.dao.MoviePersonsSettingsDao
import com.example.my_movie_search.model.room.dao.MovieTrailersSettingsDao
import com.example.my_movie_search.model.room.dao.PersonsDao
import com.example.my_movie_search.model.room.dao.TrailersDao
import com.example.my_movie_search.model.room.entity.CountryDbEntity
import com.example.my_movie_search.model.room.entity.GenresDbEntity
import com.example.my_movie_search.model.room.entity.MovieCountrySettingsDbEntity
import com.example.my_movie_search.model.room.entity.MovieDbEntity
import com.example.my_movie_search.model.room.entity.MovieGenresSettingsDbEntity
import com.example.my_movie_search.model.room.entity.MoviePersonsSettingsDbEntity
import com.example.my_movie_search.model.room.entity.MovieTrailersSettingsDbEntity
import com.example.my_movie_search.model.room.entity.PersonsDbEntity
import com.example.my_movie_search.model.room.entity.TrailersDbEntity

@Database(
    entities = [
        MovieDbEntity::class,
        PersonsDbEntity::class,
        MoviePersonsSettingsDbEntity::class,
        TrailersDbEntity::class,
        MovieTrailersSettingsDbEntity::class,
        CountryDbEntity::class,
        MovieCountrySettingsDbEntity::class,
        GenresDbEntity::class,
        MovieGenresSettingsDbEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    abstract fun getPersonsDao(): PersonsDao

    abstract fun getTrailersDao(): TrailersDao

    abstract fun getCountryDao(): CountryDao

    abstract fun getGenresDao(): GenresDao

    abstract fun getMoviePersonsSettingsDao(): MoviePersonsSettingsDao

    abstract fun getMovieTrailersSettingsDao(): MovieTrailersSettingsDao

    abstract fun getMovieCountrySettingsDao(): MovieCountrySettingsDao

    abstract fun getMovieGenresSettingsDao(): MovieGenresSettingsDao

    abstract fun getInsertMovieDao(): InsertMovieDao
}