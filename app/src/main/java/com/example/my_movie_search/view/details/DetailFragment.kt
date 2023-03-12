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
import com.example.my_movie_search.viewModel.MainViewModel

class DetailFragment : Fragment() {
    private lateinit var dataModel: MainViewModel

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

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dataModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val observer = Observer<Movie> {
            renderData(it)
        }

        dataModel.getLiveDataDetail().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(it: Movie) {
        binding.apply {
            ivMovieDetail.setImageResource(it.imageId)
            tvName.text = it.name
            tvGenre.text = it.genre
            tvAuthor.text = it.author.toString()
            tvShortDescription.text = it.shortDescription
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



