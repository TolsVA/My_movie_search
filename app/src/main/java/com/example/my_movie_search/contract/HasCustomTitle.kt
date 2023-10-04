package com.example.my_movie_search.contract

import androidx.annotation.StringRes

interface HasCustomTitle {
    @StringRes
    fun getTitleRes(): Int

    fun getTitle(): String
}