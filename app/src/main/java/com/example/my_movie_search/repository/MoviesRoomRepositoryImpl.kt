package com.example.my_movie_search.repository

import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.room.AppDatabase
import com.example.my_movie_search.view.MainActivity
import com.example.my_movie_search.viewModel.AppState

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

    override fun getAllMovie(): MutableLiveData<AppState> {
        val liveData: MutableLiveData<AppState> = MutableLiveData()
        MainActivity.getHandler().post {
            liveData.postValue(
                AppState.Success(
                    appDb.getMovieDao().getAllMovie().map { movieDbEntity ->
                        val movie = movieDbEntity.toMovie()
                        movie.persons =
                            appDb.getPersonsDao().findByIdMoviePersons(movie.idRow)
                                .map { personsDbEntity ->
                                    personsDbEntity.toPersons()
                                }
                        movie.countries =
                            appDb.getCountryDao().findByIdMovieCountry(movie.idRow)
                                .map {countryDbEntity ->
                                    countryDbEntity.toCountry()
                                }
                        movie.genres =
                            appDb.getGenresDao().findByIdMovieGenres(movie.idRow)
                                .map {genresDbEntity ->
                                    genresDbEntity.toGenres()
                                }
                        movie
                    } as MutableList<Movie>
                )
            )
        }
        return liveData
    }

    override fun findByIdMoviePersons(idRow: Long) =
        appDb.getPersonsDao().findByIdMoviePersons(idRow)
}