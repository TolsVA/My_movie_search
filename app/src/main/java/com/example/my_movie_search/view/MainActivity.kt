package com.example.my_movie_search.view

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_movie_search.R
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.RepositoryImpl
import com.example.my_movie_search.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var repository = RepositoryImpl
    private fun newMovie(index: Int, icons: TypedArray, nameMovie: TypedArray): Movie {
        return Movie(
            icons.getResourceId(index, 0),
            "${resources.getString(nameMovie.getResourceId(index, 0))} $index"
        )
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val iconsPortraitWorld: TypedArray =
            resources.obtainTypedArray(R.array.world_cover_portrait)
        val iconsLandscapeWorld: TypedArray =
            resources.obtainTypedArray(R.array.world_cover_landscape)

        val iconsPortraitRus: TypedArray = resources.obtainTypedArray(R.array.rus_cover_portrait)
        val iconsLandscapeRus: TypedArray = resources.obtainTypedArray(R.array.rus_cover_landscape)

        val nameMovieWorld: TypedArray = resources.obtainTypedArray(R.array.name_world_film)

        val listMoviePortraitWorld: MutableList<Movie> = mutableListOf()
        val listMovieLandscapeWorld: MutableList<Movie> = mutableListOf()
        val listMoviePortraitRus: MutableList<Movie> = mutableListOf()
        val listMovieLandscapeRus: MutableList<Movie> = mutableListOf()
        val range = 0 until iconsPortraitWorld.length()
        for (index in range) {
            listMoviePortraitWorld.add(newMovie(index, iconsPortraitWorld, nameMovieWorld))
            listMovieLandscapeWorld.add(newMovie(index, iconsLandscapeWorld, nameMovieWorld))
            listMoviePortraitRus.add(newMovie(index, iconsPortraitRus, nameMovieWorld))
            listMovieLandscapeRus.add(newMovie(index, iconsLandscapeRus, nameMovieWorld))
        }
        val arrListOfList = mutableListOf<MutableList<Movie>>()
        arrListOfList.add(listMoviePortraitWorld)
        arrListOfList.add(listMovieLandscapeWorld)
        arrListOfList.add(listMoviePortraitRus)
        arrListOfList.add(listMovieLandscapeRus)
        repository.addListMovie(arrListOfList)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}