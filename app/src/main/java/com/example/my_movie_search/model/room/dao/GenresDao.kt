package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_movie_search.model.room.entity.GenresDbEntity

@Dao
interface GenresDao {
    @Insert(entity = GenresDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genresDbEntity: GenresDbEntity): Long

    @Query("SELECT * FROM genres WHERE name = :name")
    fun findByGenres(name: String): Long
}