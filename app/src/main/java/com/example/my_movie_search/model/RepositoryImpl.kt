package com.example.my_movie_search.model

object RepositoryImpl : Repository {
    private lateinit var listMovies: MutableList<MutableList<Movie>>

    override fun getMovieFromLocalStoragePortWorld(): MutableList<Movie> = getListMovie(0)
    override fun getMovieFromLocalStorageLandWorld(): MutableList<Movie> = getListMovie(1)
    override fun getMovieFromLocalStoragePortraitRus(): MutableList<Movie> = getListMovie(2)
    override fun getMovieFromLocalStorageLandscapeRus(): MutableList<Movie> = getListMovie(3)

    private fun getListMovie(index: Int): MutableList<Movie> = listMovies[index]

    fun addListMovie(listMovies: MutableList<MutableList<Movie>>) {
        this.listMovies = listMovies
    }
}