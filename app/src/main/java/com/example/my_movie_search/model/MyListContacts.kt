package com.example.my_movie_search.model

import android.os.Parcelable
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.google.gson.annotations.SerializedName

data class MyContact(
    val name: String?,
    val photo: String?,
    val phone: String
) : AdapterItem


