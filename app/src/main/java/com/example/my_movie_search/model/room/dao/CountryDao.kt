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

    @Query("SELECT country.*, movies_country_settings.* " +
            "FROM  country " +
            "INNER JOIN movies_country_settings " +
            "ON  country.id_row = movies_country_settings.country_id_row " +
            "AND movies_country_settings.movie_id_row = :idRow")
    fun findByIdMovieCountry(idRow: Long): MutableList<CountryDbEntity>
}