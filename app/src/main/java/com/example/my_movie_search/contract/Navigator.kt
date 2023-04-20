package com.example.my_movie_search.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showDetailPersonsScreen(persons: Persons)
//    fun showDetailPersonsScreen()

//    fun showDetailMovieScreen(movie: Movie)
    fun showDetailMovieScreen()

    fun goBack()

    fun goToMenu()

    fun <T : Parcelable> publishResult(result: T)

    fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>)

}