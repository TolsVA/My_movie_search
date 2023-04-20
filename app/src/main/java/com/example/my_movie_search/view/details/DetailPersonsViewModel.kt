package com.example.my_movie_search.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.repository.Callback
import com.example.my_movie_search.repository.MoviesRepository
import com.example.my_movie_search.repository.MoviesRepositoryImpl
import com.example.my_movie_search.repository.retrofit.RemoteDataSource
import com.example.my_movie_search.viewModel.AppState

class DetailPersonsViewModel(
    private val messageDetailPersonsFragment: MutableLiveData<Persons> = MutableLiveData(),
    private val messageMoviesPersonsFragment: MutableLiveData<AppState> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository = MoviesRepositoryImpl(RemoteDataSource())
) : ViewModel() {
    var id: Long? = null

    fun getLiveDataDetailPersons() = messageDetailPersonsFragment

    fun getLiveDataMoviesPersons() = messageMoviesPersonsFragment

    fun getPersonsMovie(persons: Persons?) {
        id = persons?.id
        messageMoviesPersonsFragment.value = AppState.Loading
        moviesRepositoryImpl.getMoviesPersonsFromSQLite(id, callBackMPLocal)
    }

    private val callBackMPLocal = object : Callback<MutableList<Movie>> {
        override fun onSuccess(result: MutableList<Movie>) {
            messageMoviesPersonsFragment.postValue(AppState.Success(result))
//            moviesRepositoryImpl.getMovieFromNetServer(id!!, callBack)
        }

        override fun onError(error: Throwable?) {

        }
    }
}