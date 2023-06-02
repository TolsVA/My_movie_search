package com.example.my_movie_search.model.room.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.example.my_movie_search.app.App.Companion.getAppDb
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

    @Transaction
    fun insertMoviesTransaction(movies: MutableList<Movie>) {
        movies.forEach { movie ->
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

    fun insertMovies(movieDbEntity: MovieDbEntity) =
        getAppDb().getMovieDao().insertMovies(movieDbEntity)

    fun insertPersons(personsDbEntity: PersonsDbEntity) =
        getAppDb().getPersonsDao().insertPersons(personsDbEntity)

    fun insertMoviePersonsSettings(moviePersonsSettingsDbEntity: MoviePersonsSettingsDbEntity) {
        getAppDb().getMoviePersonsSettingsDao()
            .insertMoviePersonsSettings(moviePersonsSettingsDbEntity)
    }

    fun insertTrailers(trailersDbEntity: TrailersDbEntity) =
        getAppDb().getTrailersDao().insertTrailers(trailersDbEntity)

    fun insertMovieTrailersSettings(movieTrailersSettingsDbEntity: MovieTrailersSettingsDbEntity) {
        getAppDb().getMovieTrailersSettingsDao()
            .insertMovieTrailersSettings(movieTrailersSettingsDbEntity)
    }

    fun insertCountry(countryDbEntity: CountryDbEntity) =
        getAppDb().getCountryDao().insertCountry(countryDbEntity)

    fun findByCountry(name: String) =
        getAppDb().getCountryDao().findByCountry(name)

    fun insertMovieCountrySettings(movieCountrySettingsDbEntity: MovieCountrySettingsDbEntity) {
        getAppDb().getMovieCountrySettingsDao()
            .insertMovieCountrySettings(movieCountrySettingsDbEntity)
    }

    fun insertGenres(genresDbEntity: GenresDbEntity) =
        getAppDb().getGenresDao().insertGenres(genresDbEntity)

    fun findByGenres(name: String) =
        getAppDb().getGenresDao().findByGenres(name)

    fun insertMovieGenresSettings(movieGenresSettingsDbEntity: MovieGenresSettingsDbEntity) {
        getAppDb().getMovieGenresSettingsDao()
            .insertMovieGenresSettings(movieGenresSettingsDbEntity)
    }
}