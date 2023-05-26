package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.my_movie_search.model.room.entity.MovieGenresSettingsDbEntity

@Dao
interface MovieGenresSettingsDao {
    @Insert(entity = MovieGenresSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieGenresSettings(movieGenresSettingsDbEntity: MovieGenresSettingsDbEntity)
}