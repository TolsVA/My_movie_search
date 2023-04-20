package com.example.my_movie_search.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.my_movie_search.view.hide
import com.example.my_movie_search.view.show
import com.example.my_movie_search.view.showSnackBar
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var filter = ""

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            getLiveDataNet().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }

        with(binding) {
            btFilter.setOnClickListener {
                filter = etFilter.text.toString()
                progressNet.show()
                adapter.clearList()
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
                        {}
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
                        { viewModel.getMovie(filter) }
                    )
                }
                is AppState.SuccessPersons -> TODO()
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>) {
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
                            viewModel.getLiveDataDetail().value = it
                            navigator().showDetailMovieScreen()
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
