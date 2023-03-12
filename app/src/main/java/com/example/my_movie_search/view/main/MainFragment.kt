package com.example.my_movie_search.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.adapters.ItemAdapter.OnClickItem
import com.example.my_movie_search.databinding.FragmentMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.details.DetailFragment
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    private var isRus: Boolean = true
    private var pref: SharedPreferences? = null
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        private const val ARG_RUS_REY = "ARG_RUS_REY"
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = requireActivity().getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        isRus = pref!!.getBoolean(ARG_RUS_REY, true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val editor: SharedPreferences.Editor = pref!!.edit()
        editor
            .putBoolean(ARG_RUS_REY, isRus)
            .apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val observerPortrait = Observer<AppState> {
            renderData(it, true)
        }
        viewModel.getLiveDataPortrait().observe(viewLifecycleOwner, observerPortrait)

        val observerLandscape = Observer<AppState> {
            renderData(it, false)
        }
        viewModel.getLiveDataLandscape().observe(viewLifecycleOwner, observerLandscape)

        if (savedInstanceState == null) {
            viewModel.getMovie(!isRus)
        }

        binding.apply {
            fabSetIcon(!isRus, fab)
            fab.setOnClickListener {
                fabSetIcon(isRus, fab)
                viewModel.getMovie(isRus)
                isRus = !isRus
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun fabSetIcon(b: Boolean, fab: FloatingActionButton) {
        if (b) {
            fab.foreground = resources.getDrawable(
                R.drawable.earth,
                requireContext().theme
            )
        } else {
            fab.foreground = resources.getDrawable(
                R.drawable.flag_of_russia,
                requireContext().theme
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun renderData(appState: AppState, b: Boolean) {
        binding.apply {
            val progress: ProgressBar = if (b) {
                progressHorizontal
            } else {
                progressVertical
            }
            when (appState) {
                is AppState.Success -> {
                    val listMovies = appState.listMovies
                    progress.visibility = View.GONE
                    setData(listMovies, b)
                }
                is AppState.Loading -> {
                    progress.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    progress.visibility = View.GONE
                }
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>, b: Boolean) {
        val adapter = ItemAdapter()
        binding.apply {
            if (b) {
                rvListHorizontal.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                rvListHorizontal.adapter = adapter
            } else {
                rvListVertical.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                rvListVertical.adapter = adapter
            }
            adapter.setLocation(b)
            adapter.addMovieList(listMovies)
            adapter.setOnClickItem(object : OnClickItem {
                override fun onClickItem(
                    movie: Movie,
                    position: Int
                ) {
                    viewModel.getLiveDataDetail().value = movie
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, DetailFragment.newInstance())
                        .addToBackStack("ff")
                        .commit()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


