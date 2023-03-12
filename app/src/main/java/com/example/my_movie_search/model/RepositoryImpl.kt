package com.example.my_movie_search.model

object RepositoryImpl : Repository {
    private lateinit var listMoviePortraitWorld: MutableList<Movie>
    private lateinit var listMovieLandscapeWorld: MutableList<Movie>
    private lateinit var listMoviePortraitRus: MutableList<Movie>
    private lateinit var listMovieLandscapeRus: MutableList<Movie>

    override fun getMovieFromServer(): Movie {
        return Movie(55, "")
    }

    override fun getMovieFromLocalStoragePortraitWorld(): MutableList<Movie> {
        return listMoviePortraitWorld
    }
    override fun getMovieFromLocalStorageLandscapeWorld(): MutableList<Movie> {
        return listMovieLandscapeWorld
    }

    override fun getMovieFromLocalStoragePortraitRus(): MutableList<Movie> {
        return listMoviePortraitRus
    }
    override fun getMovieFromLocalStorageLandscapeRus(): MutableList<Movie> {
        return listMovieLandscapeRus
    }

    fun addListMovie(listMovie: MutableList<MutableList<Movie>>) {
        listMoviePortraitWorld = listMovie[0]
        listMovieLandscapeWorld = listMovie[1]
        listMoviePortraitRus = listMovie[2]
        listMovieLandscapeRus = listMovie[3]
    }
}