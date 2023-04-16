package com.example.my_movie_search.repository.retrofit

import com.example.my_movie_search.model.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MoviesAPI {
    @GET("v1/movie") fun getMovies(
        @Header("x-api-key") token: String,
        @Query("selectFields") selectFields: String
        = "id name type rating description year poster genres countries persons movieLength videos",
        @Query("name") filter: String,
        @Query("limit") limit: Int = 10
    ): Call<MovieList>
}