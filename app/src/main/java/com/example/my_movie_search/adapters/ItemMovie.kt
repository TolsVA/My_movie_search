package com.example.my_movie_search.adapters

import com.example.my_movie_search.model.Movie

class ItemMovie(item: Movie) : AdapterItem {
    private val movie: Movie

    fun getMovie(): Movie {
        return movie
    }

    init {
        movie = item
    }
}
