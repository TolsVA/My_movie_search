package com.example.my_movie_search.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Genres
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.model.room.AppDatabase
import com.example.my_movie_search.view.MainActivity
import com.example.my_movie_search.viewModel.AppState

class RoomRepositoryImpl(private val appDb: AppDatabase) : RoomRepository {

    override fun insertMovieToDb(movies: MutableList<Movie>) {
        MainActivity.getHandler().post {
            appDb.getInsertMovieDao().insertMoviesTransaction(movies)
        }
    }

    override fun getAllMovieFilter(
        filter: String,
        callBackRoomMovieFilter: Callback<MutableList<Movie>>
    ) {
        MainActivity.getHandler().post {
            callBackRoomMovieFilter.onSuccess(appDb.getMovieDao().getFindByFilter(filter))
        }
    }

    override fun getAllMovie() = appDb.getMovieDao().getAllMovie().map { listMovieDbEntity ->
        val appState: AppState = AppState.Success(
            listMovieDbEntity.map { movieDbEntity ->
                val movie = movieDbEntity.toMovie()
                fullMovie(movie)
                movie
            } as MutableList<Movie>)
        appState
    } as MutableLiveData<AppState>

    private fun fullMovie(movie: Movie) {
        MainActivity.getHandler().post {
            movie.persons = findByIdMoviePersons(movie.idRow)
            movie.countries = findByIdMovieCountry(movie.idRow)
            movie.genres = findByIdMovieGenres(movie.idRow)
        }
    }

    override fun findByIdMoviePersons(idRow: Long): List<Persons> {
        return appDb.getPersonsDao().findByIdMoviePersons(idRow).map { personsDbEntity ->
            personsDbEntity.toPersons()
        }
    }

    override fun findByIdMovieCountry(idRow: Long): List<Country> {
        return appDb.getCountryDao().findByIdMovieCountry(idRow).map { countryDbEntity ->
            countryDbEntity.toCountry()
        }
    }

    override fun findByIdMovieGenres(idRow: Long): List<Genres> {
        return appDb.getGenresDao().findByIdMovieGenres(idRow).map { genresDbEntity ->
            genresDbEntity.toGenres()
        }
    }

    override fun getMoviesPersonsId(
        personId: Long,
        callBackLocalIdPersons: Callback<List<Movie>>
    ) {
        MainActivity.getHandler().post {
            callBackLocalIdPersons.onSuccess(appDb.getMovieDao().getMoviePersonsId(personId))
        }
    }

    override fun getMovie(id: Long): LiveData<Movie> {
        return appDb.getMovieDao().findByIdMovie(id).map {
            val movie = it.toMovie()
            fullMovie(movie)
            movie
        }
    }
}