package com.example.my_movie_search.repository.retrofit

import com.example.my_movie_search.model.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PersonsIdAPI {
    @GET("v1/person") fun getPersonsIdAPI(
        @Header("x-api-key") token: String,
        @Query("selectFields") selectFields: String = "id movies",
        @Query("id") id: Long,
        @Query("limit") limit: Int = 1
    ): Call<MovieList>
}