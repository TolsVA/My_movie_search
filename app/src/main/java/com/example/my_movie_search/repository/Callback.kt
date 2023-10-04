package com.example.my_movie_search.repository

interface Callback<T> {
    fun onSuccess(result: T)
    fun onError(error: Throwable?)
}