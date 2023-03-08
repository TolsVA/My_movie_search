package com.example.my_movie_search.viewModel

import com.example.my_movie_search.model.Movie

sealed class AppState {
    data class Success(val listMovies: ArrayList<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}