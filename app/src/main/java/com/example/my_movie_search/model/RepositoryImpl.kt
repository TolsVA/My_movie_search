package com.example.my_movie_search.model

object RepositoryImpl : Repository {
    private var listMovie: ArrayList<Movie>? = null

    override fun getMovieFromServer(): Movie {
        return Movie(55, "")
    }

    override fun getMovieFromLocalStorage(): ArrayList<Movie> {
        return listMovie!!
    }

    fun addListMovie(listMovie: ArrayList<Movie>) {
        this.listMovie = listMovie
    }
}