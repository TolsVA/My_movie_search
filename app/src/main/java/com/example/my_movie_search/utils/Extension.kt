package com.example.my_movie_search.view

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE }
    return this
}

fun View.hide() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE }
    return this
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun TextView.mySetText(name: String?) { this.text = name }