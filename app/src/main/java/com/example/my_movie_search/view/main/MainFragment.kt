package com.example.my_movie_search.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
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
import com.example.my_movie_search.view.hide
import com.example.my_movie_search.view.show
import com.example.my_movie_search.view.showSnackBar
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private val pref: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("TABLE", Context.MODE_PRIVATE)
    }

    private val adapterHorizontal = ItemAdapter()
    private val adapterVertical = ItemAdapter()
    private var isRus: Boolean = true
    private var flag: Boolean = true
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        private const val ARG_RUS_REY = "ARG_RUS_REY"

        fun newInstance() = MainFragment()
    }

    override fun onPause() {
        super.onPause()
        flag = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRus = pref.getBoolean(ARG_RUS_REY, true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pref.edit().putBoolean(ARG_RUS_REY, isRus).apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observerPortrait = getObserver(true)
        val observerLandscape = getObserver(false)

        viewModel.apply {
            getLiveDataPortrait().observe(viewLifecycleOwner, observerPortrait)
            getLiveDataLandscape().observe(viewLifecycleOwner, observerLandscape)

            if (flag) {
                getMovie(!isRus)
            }
        }

        with(binding) {
            fabSetIcon(!isRus, fab)

            fab.setOnClickListener {
                fabSetIcon(isRus, fab)
                viewModel.getMovie(isRus)
                isRus = !isRus
            }
        }
    }

    private fun fabSetIcon(b: Boolean, fab: FloatingActionButton) {
        fab.foreground = ResourcesCompat.getDrawable(
            resources,
            when (b) {
                true -> R.drawable.earth
                false -> R.drawable.flag_of_russia
            },
            requireContext().theme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun renderData(appState: AppState, b: Boolean) {
        with(binding) {
            val progress: ProgressBar = when (b) {
                true -> progressHorizontal
                false -> progressVertical
            }

            when (appState) {
                is AppState.Success -> {
                    progress.hide()
                    setData(appState.listMovies, b)
                }

                is AppState.Loading -> { progress.show() }

                is AppState.Error -> {
                    progress.hide()
                    progress.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getMovie(!isRus) }
                    )
                }
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>, b: Boolean) {
        binding.apply {
            val adapter = when (b) {
                true -> {
                    rvListHorizontal.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ).also { rvListHorizontal.adapter = adapterHorizontal }
                    adapterHorizontal
                }

                false -> {
                    rvListVertical.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    ).also { rvListVertical.adapter = adapterVertical }
                    adapterVertical
                }
            }

            adapter.apply {
                setLocation(b)
                addMovieList(listMovies)
                setOnClickItem(object : OnClickItem {
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
    }

    private fun getObserver(b: Boolean) = Observer<AppState> { renderData(it, b) }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapterVertical.setOnClickItem(null)
        adapterHorizontal.setOnClickItem(null)
    }
}