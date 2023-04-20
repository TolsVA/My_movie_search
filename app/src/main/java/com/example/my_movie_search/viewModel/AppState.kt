package com.example.my_movie_search.viewModel

import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons

sealed class AppState {
    data class Success(val listMovies: MutableList<Movie>) : AppState()
    data class SuccessPersons(val listPersons: MutableList<Persons>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class ResponseEmpty(val message: String) : AppState()
    object Loading : AppState()
}

//sealed class AppState<T> {
//    data class Success(val data: T) : AppState<T>()
////    data class SuccessPersons(val listPersons: MutableList<Persons>) : AppState ()
////    data class Error(val error: Throwable) : AppState()
////    data class ResponseEmpty(val message: String) : AppState()
////    object Loading : AppState()
//}