package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_search.databinding.FragmentDetailBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.mySetText
import com.example.my_movie_search.viewModel.MainViewModel
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private val dataModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val observer = Observer<Movie> { movie ->
            renderData(movie)
        }

        dataModel.getLiveDataDetail().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(movie: Movie) {
        binding.apply {

            Picasso.with(context)
                .load(movie.poster?.url)
                .into(ivMovieDetail)

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
        fun newInstance() = DetailFragment()
    }
}