package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_movie_search.model.room.entity.CountryDbEntity

@Dao
interface CountryDao {
    @Insert(entity = CountryDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(countryDbEntity: CountryDbEntity): Long

    @Query("SELECT * FROM country WHERE name = :name")
    fun findByCountry(name: String): Long
}