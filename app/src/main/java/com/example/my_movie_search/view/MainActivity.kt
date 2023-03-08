package com.example.my_movie_search.view

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_movie_search.R
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.RepositoryImpl

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var repository = RepositoryImpl

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val icons: TypedArray = resources.obtainTypedArray(R.array.avatar)
        val nameMovie: TypedArray = resources.obtainTypedArray(R.array.nameAvatar)

        val listMovie = arrayListOf<Movie>()
        val range = 0 until icons.length()
        for (index in range) {
            listMovie.add(
                Movie(
                    icons.getResourceId(index, 0),
                    resources.getString(nameMovie.getResourceId(index, 0))
                )
            )
        }
        repository.addListMovie(listMovie)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}