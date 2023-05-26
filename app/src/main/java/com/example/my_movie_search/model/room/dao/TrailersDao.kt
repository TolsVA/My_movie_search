package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.my_movie_search.model.room.entity.TrailersDbEntity

@Dao
interface TrailersDao {
    @Insert(entity = TrailersDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailers(trailersDbEntity: TrailersDbEntity): Long
}