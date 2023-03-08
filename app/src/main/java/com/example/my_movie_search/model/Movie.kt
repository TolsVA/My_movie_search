package com.example.my_movie_search.model

data class Movie(
    var imageId: Int,
    var name: String?,
    val genre: String = "Комедия", //Жанр
    val author: Author = getDefaultAuthor(),
    val shortDescription: String = "Краткое описание"
)

fun getDefaultAuthor() = Author(22, "Комик")