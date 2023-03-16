package com.example.my_movie_search.model

data class Author(val imageId: Int, val title: String, val age: Int = randomAge())
fun randomAge(): Int = (0..30).random()