package com.example.my_movie_search.view.details

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.Docs
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.repository.Callback
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.viewModel.AppState
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
private const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class DetailPersonsViewModel(
    private val messageDetailPersonsFragment: MutableLiveData<Persons> = MutableLiveData(),
    private val messageMoviesPersonsFragment: MutableLiveData<AppState> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource())
) : ViewModel() {
    private var idPerson: Long? = null
    lateinit var moviesPersonIdNetServer: MutableList<Movies>

    fun getLiveDataDetailPersons() = messageDetailPersonsFragment

    fun getLiveDataMoviesPersons() = messageMoviesPersonsFragment

    fun getPersonsMovie(persons: Persons?) {
        idPerson = persons?.id
        messageMoviesPersonsFragment.value = AppState.Loading
        if (idPerson != null) {
            moviesRepositoryImpl.getMoviePersonsIdFromNetServer(idPerson!!, callBack)
        } else {
            Toast.makeText(
                App.appInstance.applicationContext,
                "Нет id у актера поиск невозьожен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getMoviePersonsId(docs: MutableList<Docs>) {
        for (movies in docs) {
            Log.d("MyLog","movies.movies.toString() -> ${movies.movies}")

            moviesPersonIdNetServer = movies.movies
            moviesRepositoryImpl.getMovieFromSQLite(moviesPersonIdNetServer, callBackLocalIdMovie)
        }
    }

    private val callBackLocalIdMovie = object : Callback<MutableList<Movie>> {
        override fun onSuccess(result: MutableList<Movie>) {
            val requestSheet = mutableListOf<Movies>()
            val booleanList = mutableListOf<Boolean>()
            if (result.size > 0) {
                messageMoviesPersonsFragment.postValue(AppState.Success(result))
                for (index in 0 until moviesPersonIdNetServer.size) {
                    booleanList.add(true)
                    for (movieLocal in result) {
                        if (moviesPersonIdNetServer[index].id == movieLocal.id) {
                            booleanList[index] = false
                        }
                    }
                }

                for (index in 0 until booleanList.size) {
                    if (booleanList[index]) {
                        requestSheet.add(moviesPersonIdNetServer[index])
                    }
                }
            }
            getMovie(requestSheet)
        }

        override fun onError(error: Throwable?) {

        }
    }

    private fun getMovie(movies: MutableList<Movies>) {
        if (movies.size > 0) {
            for (movie in movies) {
                moviesRepositoryImpl.getMovieFromNetServer(movie.id!!, callBackId)
            }
        }
    }

    private val callBack = object : retrofit2.Callback<MovieListPersonsId> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieListPersonsId>, response: Response<MovieListPersonsId>) {
            val serverResponse: MovieListPersonsId? = response.body()

            if (response.isSuccessful && serverResponse != null) {
                if (serverResponse.docs.size > 0) {
                    getMoviePersonsId(serverResponse.docs)
                } else {
                    messageMoviesPersonsFragment.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
                }
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieListPersonsId>, t: Throwable) {
            messageMoviesPersonsFragment.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }

    private val callBackId = object : retrofit2.Callback<MovieList> {
        @Throws(IOException::class)
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                if (serverResponse.movies.size > 0) {
                    messageMoviesPersonsFragment.postValue(AppState.Success(serverResponse.movies))
                    moviesRepositoryImpl.insertMovieToDb(serverResponse.movies)
                } else {
                    messageMoviesPersonsFragment.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
                }
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            messageMoviesPersonsFragment.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }
}