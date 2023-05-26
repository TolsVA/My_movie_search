package com.example.my_movie_search.repository

import androidx.room.Transaction
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.room.AppDatabase
import com.example.my_movie_search.view.MainActivity

class MoviesRoomRepositoryImpl(private val appDb: AppDatabase) : MoviesRoomRepository {

    override fun getMovieFromRoom(
        filter: String,
        callBackLocal: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
//            callBackLocal.onSuccess(
//                appDb.getMovieDao().findByFilter(filter).map {
//                    it.toMovie()
//                } as MutableList<Movie>
//            )
        }
    }

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
//            insert(movies)
            appDb.getInsertMovieDao().insertMoviesTransaction(movies)
        }
    }

    @Transaction
    override fun insertEntity(movies: MutableList<Movie>) {
//        MainActivity.getHandler().post {
//            //            insert(movies)
//            appDb.getMovieDao().insertMovies(
//                movies.map { MovieDbEntity.fromMovieData(it) }
//            )
//        }
//        Thread{
//            appDb.getMovieDao().insertMovies(
//                movies.map { MovieDbEntity.fromMovieData(it) }
//            )
//        }.start()
    }


    override fun getAllMovie(filter: String) = appDb.getMovieDao().getAllMovie(filter)

    override fun findByIdMoviePersons(idRow: Long) = appDb.getPersonsDao().findByIdMoviePersons(idRow)

//    override fun findByIdMoviePersons(idRow: Long): MutableList<Persons> {
//        val listPersons = appDb.getPersonsDao().findByIdMoviePersons(idRow).map {
//            it.toPersons()
//        } as MutableList<Persons>
//        return listPersons
//    }]

//    fun insert(movies: MutableList<Movie>) {
//        movies.map { movie ->
//            movie.idRow = appDb.getMovieDao().insertMovies(fromMovieData(movie))
//            movie.persons.forEach { persons ->
//                if (persons.name != null && persons.id != null) {
//                    persons.idRow = appDb.getPersonsDao().insertPersons(
//                        fromPersonsData(persons)
//                    )
//                    appDb.getMoviePersonsSettingsDao().insertMoviePersonsSettings(
//                        fromMoviePersonsSettingsDbEntity(
//                            movie,
//                            persons
//                        )
//                    )
//                }
//            }
//            movie.videos?.trailers?.forEach { trailer ->
//                if (trailer.url != null) {
//                    trailer.idRow = appDb.getTrailersDao().insertTrailers(
//                        fromTrailersData(trailer)
//                    )
//                    appDb.getMovieTrailersSettingsDao().insertMovieTrailersSettings(
//                        fromMovieTrailersSettingsDbEntity(
//                            movie,
//                            trailer
//                        )
//                    )
//                }
//            }
//            movie.countries.forEach { countries ->
//                if (countries.name != null) {
//                    countries.idRow = appDb.getCountryDao().findByCountry(countries.name)
//                    if (countries.idRow < 1) {
//                        countries.idRow = appDb.getCountryDao().insertCountry(
//                            fromCountryData(countries)
//                        )
//                    }
//                    appDb.getMovieCountrySettingsDao().insertMovieCountrySettings(
//                        fromMovieCountrySettingsDbEntity(
//                            movie,
//                            countries
//                        )
//                    )
//                }
//            }
//            movie.genres.forEach { genres ->
//                if (genres.name != null) {
//                    genres.idRow = appDb.getGenresDao().findByGenres(genres.name)
//                    if (genres.idRow < 1) {
//                        genres.idRow = appDb.getGenresDao().insertGenres(
//                            fromGenresData(genres)
//                        )
//                    }
//                    appDb.getMovieGenresSettingsDao().insertMovieGenresSettings(
//                        fromMovieGenresSettingsDbEntity(
//                            movie,
//                            genres
//                        )
//                    )
//                }
//            }
//        }
//    }
}