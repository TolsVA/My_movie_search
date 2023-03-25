package com.example.my_movie_search.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_movie_search.R
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}