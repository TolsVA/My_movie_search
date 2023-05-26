package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.my_movie_search.model.room.entity.MoviePersonsSettingsDbEntity

@Dao
interface MoviePersonsSettingsDao {
    @Insert(entity = MoviePersonsSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviePersonsSettings(moviePersonsSettingsDbEntity: MoviePersonsSettingsDbEntity)
}