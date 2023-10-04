package com.example.my_movie_search.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomAction {
    fun getCustomAction(): MutableList<CustomAction>
}

interface HasCustomActionBottomNavigation {
    fun getCustomActionBottomNavigation(): MutableList<CustomAction>
}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)