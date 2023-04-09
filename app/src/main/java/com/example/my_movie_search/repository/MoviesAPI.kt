package com.example.my_movie_search.repository

import com.example.my_movie_search.model.MovieListNet
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MoviesAPI {
    @GET("v1/movie") fun getMovies(
        @Header("x-api-key") token: String,
        @Query("name") filter: String
    ): Call<MovieListNet>
}