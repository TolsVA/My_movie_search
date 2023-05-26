package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.my_movie_search.model.room.entity.MovieTrailersSettingsDbEntity

@Dao
interface MovieTrailersSettingsDao {
    @Insert(entity = MovieTrailersSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieTrailersSettings(movieTrailersSettingsDbEntity: MovieTrailersSettingsDbEntity)
}