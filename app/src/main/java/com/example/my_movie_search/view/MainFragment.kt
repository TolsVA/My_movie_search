package com.example.my_movie_search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.adapters.ItemAdapter.OnClickItem
import com.example.my_movie_search.databinding.FragmentMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.example.my_movie_search.databinding.ItemMovieBinding

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private val adapter = ItemAdapter()
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val observer = Observer<AppState> {
            renderData(it)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getMovie()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private fun renderData(appState: AppState) {
        binding.apply {
            when (appState) {
                is AppState.Success -> {
                    val listMovies = appState.listMovies
                    progress.visibility = View.GONE
                    setData(listMovies)
                    Snackbar.make(flContainer, "AppState.Success", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload") {
                            setData(ArrayList())
                            viewModel.getMovie()
                        }
                        .show()
                }
                is AppState.Loading -> {
                    progress.visibility = View.VISIBLE
                    Snackbar.make(flContainer, "AppState.Loading", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload") { viewModel.getMovie() }
                        .show()
                }
                is AppState.Error -> {
                    progress.visibility = View.GONE
                    Snackbar.make(flContainer, "Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload") { viewModel.getMovie() }
                        .show()
                }
            }
        }
    }

    private fun setData(listMovies: ArrayList<Movie>) {
        binding.apply {
            rvList.layoutManager = GridLayoutManager(context, 3)
            rvList.adapter = adapter
            adapter.addMovieList(listMovies)
            adapter.setOnClickItem(object : OnClickItem {
                override fun onClickItem(
                    bindingItem: ItemMovieBinding,
                    movie: Movie,
                    position: Int
                ) {
                    Toast.makeText(context, movie.name, Toast.LENGTH_SHORT).show()

                    movie.name = "unknown"
                    bindingItem.tvMovie.text = movie.name
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


