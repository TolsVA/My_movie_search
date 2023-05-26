package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.room.entity.CountryDbEntity
import com.example.my_movie_search.model.room.entity.GenresDbEntity
import com.example.my_movie_search.model.room.entity.MovieCountrySettingsDbEntity
import com.example.my_movie_search.model.room.entity.MovieDbEntity
import com.example.my_movie_search.model.room.entity.MovieGenresSettingsDbEntity
import com.example.my_movie_search.model.room.entity.MoviePersonsSettingsDbEntity
import com.example.my_movie_search.model.room.entity.MovieTrailersSettingsDbEntity
import com.example.my_movie_search.model.room.entity.PersonsDbEntity
import com.example.my_movie_search.model.room.entity.TrailersDbEntity

@Dao
interface InsertMovieDao {

    @Insert(entity = MovieDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movieDbEntity: MovieDbEntity): Long

    @Insert(entity = PersonsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPersons(personsDbEntity: PersonsDbEntity): Long

    @Insert(entity = MoviePersonsSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviePersonsSettings(moviePersonsSettingsDbEntity: MoviePersonsSettingsDbEntity)

    @Insert(entity = TrailersDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailers(trailersDbEntity: TrailersDbEntity): Long

    @Insert(entity = MovieTrailersSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieTrailersSettings(movieTrailersSettingsDbEntity: MovieTrailersSettingsDbEntity)

    @Insert(entity = CountryDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(countryDbEntity: CountryDbEntity): Long

    @Query("SELECT * FROM country WHERE name = :name")
    fun findByCountry(name: String): Long

    @Insert(entity = MovieCountrySettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieCountrySettings(movieCountrySettingsDbEntity: MovieCountrySettingsDbEntity)

    @Insert(entity = GenresDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genresDbEntity: GenresDbEntity): Long

    @Query("SELECT * FROM genres WHERE name = :name")
    fun findByGenres(name: String): Long

    @Insert(entity = MovieGenresSettingsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieGenresSettings(movieGenresSettingsDbEntity: MovieGenresSettingsDbEntity)

    @Transaction
    fun insertMoviesTransaction(movies: MutableList<Movie>) {
        movies.map { movie ->
            movie.idRow = insertMovies(MovieDbEntity.fromMovieData(movie))
            movie.persons.forEach { persons ->
                if (persons.name != null && persons.id != null) {
                    persons.idRow = insertPersons(
                        PersonsDbEntity.fromPersonsData(persons)
                    )
                    insertMoviePersonsSettings(
                        MoviePersonsSettingsDbEntity.fromMoviePersonsSettingsDbEntity(
                            movie,
                            persons
                        )
                    )
                }
            }
            movie.videos?.trailers?.forEach { trailer ->
                if (trailer.url != null) {
                    trailer.idRow = insertTrailers(
                        TrailersDbEntity.fromTrailersData(trailer)
                    )
                    insertMovieTrailersSettings(
                        MovieTrailersSettingsDbEntity.fromMovieTrailersSettingsDbEntity(
                            movie,
                            trailer
                        )
                    )
                }
            }
            movie.countries.forEach { countries ->
                if (countries.name != null) {
                    countries.idRow = findByCountry(countries.name)
                    if (countries.idRow < 1) {
                        countries.idRow = insertCountry(
                            CountryDbEntity.fromCountryData(countries)
                        )
                    }
                    insertMovieCountrySettings(
                        MovieCountrySettingsDbEntity.fromMovieCountrySettingsDbEntity(
                            movie,
                            countries
                        )
                    )
                }
            }
            movie.genres.forEach { genres ->
                if (genres.name != null) {
                    genres.idRow = findByGenres(genres.name)
                    if (genres.idRow < 1) {
                        genres.idRow = insertGenres(
                            GenresDbEntity.fromGenresData(genres)
                        )
                    }
                    insertMovieGenresSettings(
                        MovieGenresSettingsDbEntity.fromMovieGenresSettingsDbEntity(
                            movie,
                            genres
                        )
                    )
                }
            }
        }
    }
}