package com.example.my_movie_search.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_movie_search.model.room.entity.MovieDbEntity

@Dao
interface MovieDao {
    //    @Query("SELECT * FROM movies WHERE name GLOB \"* :filter *\"")
    @Query("SELECT * FROM movies WHERE name GLOB :filter")
    fun findByFilter(filter: String): MutableList<MovieDbEntity>

    @Query("SELECT * FROM movies ORDER BY name ASC")
    fun getAllMovie(): List<MovieDbEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun findByIdMovie(id: Long): MovieDbEntity

    @Insert(entity = MovieDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movieDbEntity: MovieDbEntity): Long
}