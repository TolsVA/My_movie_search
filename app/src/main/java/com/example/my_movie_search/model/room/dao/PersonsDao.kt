package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_movie_search.model.room.entity.PersonsDbEntity

@Dao
interface PersonsDao {

    @Insert(entity = PersonsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPersons(personsDbEntity: PersonsDbEntity): Long

    @Query("SELECT * FROM movies WHERE id = :id")
    fun findByIdPersons(id: Long): PersonsDbEntity

    @Query("SELECT persons.*, movies_persons_settings.* " +
            "FROM  persons " +
            "INNER JOIN movies_persons_settings " +
            "ON  persons.id_row = movies_persons_settings.persons_id_row " +
            "AND movies_persons_settings.movie_id_row = :idRow")
    fun findByIdMoviePersons(idRow: Long): MutableList<PersonsDbEntity>
}
//
//interface MovieDao {
//    //    @Query("SELECT * FROM movies WHERE name GLOB \"* :filter *\"")
//    @Query("SELECT * FROM movies WHERE name GLOB :filter")
//    fun findByFilter(filter: String): MutableList<MovieDbEntity>
//
//    @Insert(entity = MovieDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
//    fun insertMovies(moviesDbEntity: MovieDbEntity) : Long