package com.example.my_movie_search.view.details

//import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.app.App.Companion.getAppDb
import com.example.my_movie_search.model.Docs
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MovieList
import com.example.my_movie_search.model.MovieListPersonsId
import com.example.my_movie_search.model.Movies
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.repository.Callback
import com.example.my_movie_search.repository.RetrofitRepository
import com.example.my_movie_search.repository.RetrofitRepositoryImpl
import com.example.my_movie_search.repository.RoomRepository
import com.example.my_movie_search.repository.RoomRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.viewModel.AppState
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
private const val RESPONSE_EMPTY = "RESPONSE_EMPTY"

class DetailPersonsViewModel(
    private val liveDataMoviesPersons: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: RetrofitRepository = RetrofitRepositoryImpl(RemoteDataSource()),
    private val roomRepositoryImpl: RoomRepository = RoomRepositoryImpl(getAppDb())
) : ViewModel() {
    var personId: Long? = null
    private var personIdRow: Long = 0
    var moviesPersonIdLocal = listOf<Movie>()
    private var moviesPersonIdNetServer = mutableListOf<Movies>()

    fun getLiveDataMoviesPersons() = liveDataMoviesPersons

    fun getPersonsMovie(persons: Persons) {
        personId = persons.id
        personIdRow = persons.idRow
        liveDataMoviesPersons.value = AppState.Loading
        if (personId != null) {
            roomRepositoryImpl.getMoviesPersonsId(personId!!, callBackLocalIdPersons)
        }
    }

    private fun getMoviePersonsId(docs: MutableList<Docs>) {
        for (movies in docs) {
            moviesPersonIdNetServer = movies.movies
            if (moviesPersonIdNetServer.size > 0 && moviesPersonIdLocal.isNotEmpty()) {
                for (indexNet in (moviesPersonIdNetServer.size - 1) downTo 0 step 1) {
                    for (movieLocal in moviesPersonIdLocal) {
                        if (moviesPersonIdNetServer[indexNet].id == movieLocal.id ||
                            moviesPersonIdNetServer[indexNet].id == null ||
                            moviesPersonIdNetServer[indexNet].name == null
                        ) {
                            moviesPersonIdNetServer.removeAt(indexNet)
                            break
                        }
                    }
                }
            }
            if (moviesPersonIdNetServer.size > 0) {
                retrofitRepository.getMovieFromNetServer(moviesPersonIdNetServer, callBackId)
            }
        }
    }

    private val callBackLocalIdPersons = object : Callback<List<Movie>> {
        override fun onSuccess(result: List<Movie>) {
            if (result.isNotEmpty()) {
                liveDataMoviesPersons.postValue(AppState.Success(result as MutableList<Movie>))
            }
            retrofitRepository.getMoviePersonsIdFromNetServer(personId!!, callBack)
        }

        override fun onError(error: Throwable?) {

        }
    }

    private val callBack = object : retrofit2.Callback<MovieListPersonsId> {
        @Throws(IOException::class)
        override fun onResponse(
            call: Call<MovieListPersonsId>,
            response: Response<MovieListPersonsId>
        ) {
            val serverResponse: MovieListPersonsId? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                if (serverResponse.docs.size > 0) {
                    getMoviePersonsId(serverResponse.docs)
                } else {
                    liveDataMoviesPersons.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
                }
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieListPersonsId>, t: Throwable) {
            liveDataMoviesPersons.postValue(
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
                    val listMovie = mutableListOf<Movie>()
                    serverResponse.movies.forEach {
                        if (it.name != null) {
                            listMovie.add(it)
                        }
                    }
                    liveDataMoviesPersons.postValue(AppState.Success(listMovie))
                    roomRepositoryImpl.insertMovieToDb(listMovie)

//                    roomRepositoryImpl.getMoviesPersonsId(personIdRow, callBackLocalIdPersonsFull)


                } else {
                    liveDataMoviesPersons.postValue(AppState.ResponseEmpty(RESPONSE_EMPTY))
                }
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            liveDataMoviesPersons.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }

//    private val callBackLocalIdPersonsFull = object : Callback<MutableList<Movie>> {
//        override fun onSuccess(result: MutableList<Movie>) {
//            if (result.size > 0) {
//                Toast.makeText(App.appInstance, "result.size = ${result.size}", Toast.LENGTH_SHORT).show()
//                liveDataMoviesPersons.postValue(AppState.Success(result))
//            }
//        }
//
//        override fun onError(error: Throwable?) {
//
//        }
//    }
}