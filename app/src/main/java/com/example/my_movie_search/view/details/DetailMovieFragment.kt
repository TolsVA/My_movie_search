package com.example.my_movie_search.view.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.databinding.FragmentDetailMovieBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.mySetText
import com.example.my_movie_search.viewModel.MainViewModel
import com.squareup.picasso.Picasso

class DetailMovieFragment : Fragment() {

    private val dataModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var movie: Movie? = null

    private var _binding: FragmentDetailMovieBinding? = null

    private val binding
        get() = _binding!!

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = savedInstanceState?.getParcelable(KEY_MOVIE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_MOVIE, movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observer = Observer<Movie> { movie ->
            this.movie = movie
            renderData(movie)
        }
        dataModel.getLiveDataDetail().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(movie: Movie) {
        binding.apply {
            Picasso.get()
                .load(movie.poster?.url)
//                .transform(CircleTransformation())
                .into(ivMovieDetail)

            rvListPersons.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            rvListPersons.adapter = adapter.apply {

                val listItem: MutableList<AdapterItem> = mutableListOf()

                for (persons in movie.persons) {
                    listItem.add(persons)
                }

                addList(listItem)
                setOnClickItem(object : ItemAdapter.OnClickItem {
                    override fun onClickItem(
                        item: AdapterItem,
                        position: Int
                    ) {

                    }
                })
            }

            tvName.mySetText(movie.name)
            movie.genres.forEach {
                tvGenre.append("\n${it.name},")
            }

            movie.countries.forEach {
                tvAuthor.append(" ${it.name}\n")
            }

            tvShortDescription.mySetText(movie.description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        private val ARG_MOVIE = "ARG_MOVIE"

        @JvmStatic
        private val KEY_MOVIE = "KEY_MOVIE"

        //        fun newInstance() = DetailMovieFragment()
        fun newInstance(movie: Movie): DetailMovieFragment {
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            val fragment = DetailMovieFragment()
            fragment.arguments = args
            return fragment
        }
    }
}