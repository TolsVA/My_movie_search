package com.example.my_movie_search.repository.retrofit

import com.example.my_movie_search.BuildConfig
import com.example.my_movie_search.model.MovieList
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RemoteDataSource {

    private val moviesAPI = Retrofit.Builder()
        .baseUrl("https://api.kinopoisk.dev/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(MoviesApiInterceptor()))
        .build()
        .create(MoviesAPI::class.java)

    fun getMovies(filter: String, callback: Callback<MovieList>) {
        moviesAPI
            .getMovies(token = BuildConfig.MOVIE_API_KEY, filter = filter)
            .enqueue(callback)
    }

    private val movieIdAPI = Retrofit.Builder()
        .baseUrl("https://api.kinopoisk.dev/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(MoviesApiInterceptor()))
        .build()
        .create(MovieIdAPI::class.java)

    fun getMoviesID(id: Long, callback: Callback<MovieList>) {
        movieIdAPI
            .getMovieId(token = BuildConfig.MOVIE_API_KEY, id = id)
            .enqueue(callback)
    }

    private val personsIdAPI = Retrofit.Builder()
        .baseUrl("https://api.kinopoisk.dev/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(MoviesApiInterceptor()))
        .build()
        .create(PersonsIdAPI::class.java)

    fun getPersonsIdAPI(id: Long, callback: Callback<MovieList>) {
        personsIdAPI
            .getPersonsIdAPI(token = BuildConfig.MOVIE_API_KEY, id = id)
            .enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        return httpClient.build()
    }

    inner class MoviesApiInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }


}