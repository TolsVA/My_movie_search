package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.my_movie_search.model.room.entity.MovieCountrySettingsDbEntity

@Dao
interface MovieCountrySettingsDao {
    @Insert(entity = MovieCountrySettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieCountrySettings(movieCountrySettingsDbEntity: MovieCountrySettingsDbEntity)
}