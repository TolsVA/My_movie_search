package com.example.my_movie_search.model

import com.example.my_movie_search.adapters.AdapterItem

data class MyContact(
    val name: String?,
    val photo: String?,
    val phone: String,
    val sms: String?
) : AdapterItem