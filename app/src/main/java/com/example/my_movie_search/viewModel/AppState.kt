package com.example.my_movie_search.viewModel

import com.example.my_movie_search.model.Movie

sealed class AppState {
    data class Success(val listMovies: MutableList<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class ResponseEmpty(val message: String) : AppState()
    object Loading : AppState()
}