package com.example.my_movie_search.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomAction {
    fun getCustomAction(): MutableList<CustomAction>
}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)