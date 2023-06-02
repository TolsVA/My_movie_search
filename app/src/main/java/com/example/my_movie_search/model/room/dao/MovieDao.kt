package com.example.my_movie_search.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.my_movie_search.app.App.Companion.getAppDb
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Genres
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.model.room.entity.MovieDbEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE name GLOB :filter ORDER BY name ASC")
    fun findByFilter(filter: String): MutableList<MovieDbEntity>

    @Query("SELECT * FROM movies ORDER BY name ASC")
    fun getAllMovie(): LiveData<MutableList<MovieDbEntity>>


    @Query("SELECT * FROM movies WHERE id = :id")
    fun findByIdMovie(id: Long): LiveData<MovieDbEntity>

    @Insert(entity = MovieDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movieDbEntity: MovieDbEntity): Long


//    SELECT "movies".*, "movies_persons_settings"."persons_id"
//    FROM "movies"
//    INNER JOIN "movies_persons_settings"
//    ON "movies"."id_row" = "movies_persons_settings"."movie_id_row"
//    AND "movies_persons_settings"."persons_id"= '1514'

    @Query("SELECT movies.*, movies_persons_settings.persons_id_row " +
            "FROM movies " +
            "INNER JOIN movies_persons_settings " +
            "ON movies.id_row = movies_persons_settings.movie_id_row " +
            "AND movies_persons_settings.persons_id = :id")
    fun findByIdPersons(id: Long): MutableList<MovieDbEntity>

    @Transaction
    fun getMoviePersonsId(id: Long): List<Movie> {
        return findByIdPersons(id).map { movieDbEntity ->
            val movie = movieDbEntity.toMovie()
            movie.persons = findByIdMoviePersons(movie.idRow)
            movie.countries = findByIdMovieCountry(movie.idRow)
            movie.genres = findByIdMovieGenres(movie.idRow)
            movie
        }
    }

    @Transaction
    fun getFindByFilter(filter: String): MutableList<Movie> {
        return findByFilter(filter).map { movieDbEntity ->
            val movie = movieDbEntity.toMovie()
            movie.persons = findByIdMoviePersons(movie.idRow)
            movie.countries = findByIdMovieCountry(movie.idRow)
            movie.genres = findByIdMovieGenres(movie.idRow)
            movie
        } as MutableList<Movie>
    }





//    @Transaction
//    fun getMoviePersonsId(idRow: Long) = fillMovie(findByIdPersons(idRow))
//
//    fun fillMovie(movies: MutableList<MovieDbEntity>): MutableList<Movie> {
//        return movies.map { movieDbEntity ->
//            val movie = movieDbEntity.toMovie()
//            movie.persons = findByIdMoviePersons(movie.idRow)
//            movie.countries = findByIdMovieCountry(movie.idRow)
//            movie.genres = findByIdMovieGenres(movie.idRow)
//            movie
//        }as MutableList<Movie>
//    }
//
//    @Transaction
//    fun getFindByFilter(filter: String) = fillMovie(findByFilter(filter))




    fun findByIdMoviePersons(idRow: Long): List<Persons> {
        return getAppDb().getPersonsDao().findByIdMoviePersons(idRow).map { personsDbEntity ->
            personsDbEntity.toPersons()
        }
    }

    fun findByIdMovieCountry(idRow: Long): List<Country> {
        return getAppDb().getCountryDao().findByIdMovieCountry(idRow).map { countryDbEntity ->
            countryDbEntity.toCountry()
        }
    }

    fun findByIdMovieGenres(idRow: Long): List<Genres> {
        return getAppDb().getGenresDao().findByIdMovieGenres(idRow).map { genresDbEntity ->
            genresDbEntity.toGenres()
        }
    }
}