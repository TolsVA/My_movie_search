package com.example.my_movie_search.model

data class Movie(
    var imageId: Int,
    var name: String?,
    val genre: String = "Comedy",
    val author: Author = getDefaultAuthor(),
    val shortDescription: String = "Short description"
)
fun getDefaultAuthor() = Author(22, "Comedian")