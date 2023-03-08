package com.example.my_movie_search.model

data class Author(var imageId: Int, var title: String) {
    private var age: Int

    init {
        age = randomAge()
    }

    private fun randomAge(): Int = (0..30).random()
}