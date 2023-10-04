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

    @Query("SELECT genres.*, movies_genres_settings.* " +
            "FROM  genres " +
            "INNER JOIN movies_genres_settings " +
            "ON  genres.id_row = movies_genres_settings.genres_id_row " +
            "AND movies_genres_settings.movie_id_row = :idRow")
    fun findByIdMovieGenres(idRow: Long): MutableList<GenresDbEntity>
}