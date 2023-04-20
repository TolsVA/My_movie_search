package com.example.my_movie_search.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.adapters.ItemAdapter.OnClickItem
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.details.DetailViewModel
import com.example.my_movie_search.view.hide
import com.example.my_movie_search.view.show
import com.example.my_movie_search.view.showSnackBar
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var filter = ""

    private var movies = mutableListOf<Movie>()

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        @JvmStatic
        private val ARG_FILTER = "ARG_FILTER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            filter = savedInstanceState.getString(ARG_FILTER).toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_FILTER, filter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.apply {
            getLiveDataNet().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }

        with(binding) {
            btFilter.setOnClickListener {
                filter = etFilter.text.toString()
                progressNet.show()
                adapter.clearList()
                mainViewModel.getMovie(filter)
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

    private fun renderData(appState: AppState) {
        with(binding) {
            val progress = progressNet

            when (appState) {
                is AppState.Success -> {
                    progress.hide()
                    setData(appState.listMovies)
                }

                is AppState.Loading -> {
                    progress.show()
                }

                is AppState.ResponseEmpty -> {
                    progress.hide()
                    progress.showSnackBar(
                        getString(R.string.response_empty),
                        getString(R.string.ok),
                        { mainViewModel.getMovie(filter) }
                    )
                }

                is AppState.Error -> {
                    progress.hide()
                    progress.showSnackBar(
                        when (appState.error.message.toString()) {

                            "SERVER_ERROR" -> {
                                resources.getString(R.string.server_error)
                            }

                            "REQUEST_ERROR" -> {
                                resources.getString(R.string.request_error)
                            }

                            "java.net.UnknownHostException" -> {
                                getString(R.string.unknown_host_exception)
                            }

                            "java.io.FileNotFoundException" -> {
                                getString(R.string.file_not_found_exception)
                            }

                            "java.net.MalformedURLException" -> {
                                getString(R.string.malformed_url_exception)
                            }

                            "java.net.SocketTimeoutException" -> {
                                getString(R.string.socket_timeout_exception)
                            }

                            "java.lang.NullPointerException" -> {
                                "java.lang.NullPointerException"
                            }

                            else -> {
                                appState.error.message.toString()
                            }
                        },
                        getString(R.string.reload),
                        { mainViewModel.getMovie(filter) }
                    )
                }
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>) {
        movies = listMovies
        binding.apply {
            rvListNet.layoutManager = GridLayoutManager(
                context,
                3,
                GridLayoutManager.VERTICAL,
                false
            )

            rvListNet.adapter = adapter.apply {

                val listItem: MutableList<AdapterItem> = mutableListOf()

                for (movie in listMovies) {
                    listItem.add(movie)
                }

                addList(listItem)
                setOnClickItem(object : OnClickItem {
                    override fun onClickItem(
                        item: AdapterItem,
                        position: Int
                    ) {
                        (item as Movie).let {
                            detailViewModel.getLiveDataDetail().value = it
                            navigator().showDetailMovieScreen(it)
                        }
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapter.setOnClickItem(null)
    }
}
