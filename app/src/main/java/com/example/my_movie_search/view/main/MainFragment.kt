package com.example.my_movie_search.view.main

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
import com.example.my_movie_search.view.hide
import com.example.my_movie_search.view.show
import com.example.my_movie_search.view.showSnackBar
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private val adapterLocal: ItemAdapter by lazy {
        ItemAdapter()
    }

    private val adapterNet: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var filter = ""

    var localListMovies: MutableList<Movie> = mutableListOf()

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observerLocal = getObserver(true)
        val observerNet = getObserver(false)
        viewModel.apply {
            getLiveDataLocal().observe(viewLifecycleOwner, observerLocal)
            getLiveDataNet().observe(viewLifecycleOwner, observerNet)
        }

        with(binding) {
            btFilter.setOnClickListener {
                filter = etFilter.text.toString()
                viewModel.getMovie(filter)
            }
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
        with(binding) {
            val progress: ProgressBar = when (b) {
                true -> progressLocal
                false -> progressNet
            }

            when (appState) {
                is AppState.Success -> {
                    progress.hide()
                    setData(appState.listMovies, b)
                }

                is AppState.Loading -> {
                    progress.show()
                }

                is AppState.Error -> {
                    progress.hide()
                    progress.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        when(b) {
                            true -> {
                                { viewModel.getMovie(localListMovies) }
                            }
                            false -> {
                                { viewModel.getMovie(filter) }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>, b: Boolean) {
        binding.apply {
            when (b) {
                true -> {
                    localListMovies = listMovies
                    rvListLocal.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ).also { rvListLocal.adapter = adapterLocal }
                    adapterLocal
                }

                false -> {
                    rvListNet.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ).also { rvListNet.adapter = adapterNet }
                    adapterNet
                }
            }.apply {
                addMovieList(listMovies)
                setOnClickItem(object : OnClickItem {
                    override fun onClickItem(
                        movie: Movie,
                        position: Int
                    ) {
                        when(b) {
                            true -> {
                                viewModel.getLiveDataDetail().value = movie

                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.container, DetailFragment.newInstance())
                                    .addToBackStack("")
                                    .commit()
                            }
                            false -> {
                                localListMovies.add(movie)
                                viewModel.getMovie(localListMovies)
                            }

                        }

                    }
                })
            }
        }
    }

    private fun getObserver(b: Boolean) = Observer<AppState> {
        renderData( it, b )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapterNet.setOnClickItem(null)
        adapterLocal.setOnClickItem(null)
    }
}
