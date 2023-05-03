package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentDetailPersonsBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.viewModel.AppState
import com.squareup.picasso.Picasso

class DetailPersonsFragment : Fragment() {
    private val detailPersonsViewModel: DetailPersonsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailPersonsViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private lateinit var persons: Persons

    private var _binding: FragmentDetailPersonsBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        persons = savedInstanceState?.getParcelable(KEY_PERSONS)
            ?:arguments?.getParcelable(ARG_PERSONS)!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_PERSONS, persons)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPersonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        detailPersonsViewModel.apply {
            getLiveDataDetailPersons().observe(viewLifecycleOwner) {
                renderData(it)
            }
            getLiveDataMoviesPersons().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }
        renderData(persons)
        detailPersonsViewModel.getPersonsMovie(persons)
    }

    private fun renderData(appState: AppState) {
        with(binding) {
//            val progress = progressNet

            when (appState) {
                is AppState.Success -> {
//                    progress.hide()
                    setData(appState.listMovies)
                }

                is AppState.Loading -> {
//                    progress.show()
                }

                is AppState.ResponseEmpty -> {
//                    progress.hide()
//                    progress.showSnackBar(
//                        getString(R.string.response_empty),
//                        getString(R.string.ok),
//                        { detailPersonsViewModel.getPersonsMovie(persons) }
//                    )
                }

                is AppState.Error -> {
//                    progress.hide()
//                    progress.showSnackBar(
//                        when (appState.error.message.toString()) {
//
//                            "SERVER_ERROR" -> {
//                                resources.getString(R.string.server_error)
//                            }
//
//                            "REQUEST_ERROR" -> {
//                                resources.getString(R.string.request_error)
//                            }
//
//                            "java.net.UnknownHostException" -> {
//                                getString(R.string.unknown_host_exception)
//                            }
//
//                            "java.io.FileNotFoundException" -> {
//                                getString(R.string.file_not_found_exception)
//                            }
//
//                            "java.net.MalformedURLException" -> {
//                                getString(R.string.malformed_url_exception)
//                            }
//
//                            "java.net.SocketTimeoutException" -> {
//                                getString(R.string.socket_timeout_exception)
//                            }
//
//                            "java.lang.NullPointerException" -> {
//                                "java.lang.NullPointerException"
//                            }
//
//                            else -> {
//                                appState.error.message.toString()
//                            }
//                        },
//                        getString(R.string.reload),
//                        { detailPersonsViewModel.getPersonsMovie(persons) }
//                    )
                }
            }
        }
    }

    private fun setData(listMovies: MutableList<Movie>) {
        binding.apply {
            rvListMovie.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            rvListMovie.adapter = adapter.apply {

                val listItem: MutableList<AdapterItem> = mutableListOf()

                for (movie in listMovies) {
                    listItem.add(movie)
                }

                addPosition(listItem)
                setOnClickItem(object : ItemAdapter.OnClickItem {
                    override fun onClickItem(
                        item: AdapterItem,
                        position: Int
                    ) {
                        (item as Movie).let {
                            navigator().showDetailMovieScreen(it, DetailMovieFragment.TAG)
                        }
                    }
                })
            }
        }
    }


    private fun renderData(persons: Persons) {

        binding.apply {
            Picasso.get()
                .load(persons.photo)
                .into(ivPersonsDetail)

            val profession = resources.getString(R.string.profession) + " " +
                    persons.profession?.substring(0, persons.profession.length-1)

            tvTitleProfession.text = profession
            tvName.text = persons.name

            rvListMovie.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

//            rvListMovie.adapter = adapter.apply {
//
//                val listItem: MutableList<AdapterItem> = mutableListOf()
//
//                for (movie in persons.) {
//                    listItem.add(persons)
//                }
//
//                addList(listItem)
//                setOnClickItem(object : ItemAdapter.OnClickItem {
//                    override fun onClickItem(
//                        item: AdapterItem,
//                        position: Int
//                    ) {
//
//                    }
//                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "DetailPersonsFragment"
        private const val ARG_PERSONS = "ARG_PERSONS"
        private const val KEY_PERSONS = "KEY_PERSONS"

        @JvmStatic
        fun newInstance(persons: Persons) =
            DetailPersonsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PERSONS, persons)
                }
            }
    }
}