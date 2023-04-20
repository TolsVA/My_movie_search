package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.contract.CustomAction
import com.example.my_movie_search.contract.HasCustomAction
import com.example.my_movie_search.contract.HasCustomTitle
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentDetailMovieBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.view.mySetText
import com.squareup.picasso.Picasso

class DetailMovieFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailViewModel::class.java]
    }

    private val detailPersonsViewModel: DetailPersonsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailPersonsViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var _binding: FragmentDetailMovieBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observer = Observer<Movie> { movie ->
            renderData(movie)
        }
        detailViewModel.getLiveDataDetail().observe(viewLifecycleOwner, observer)
    }

    override fun getTitleRes(): Int = R.string.detailing

    override fun getCustomAction(): MutableList<CustomAction> {
        val customActionList = mutableListOf<CustomAction>()

        customActionList.add(
            CustomAction(
                iconRes = R.drawable.settings,
                textRes = R.string.settings,
                onCustomAction = Runnable {
                    Toast.makeText(requireContext(), "Пупка", Toast.LENGTH_SHORT).show()
                }
            )
        )

        customActionList.add(
            CustomAction(
                iconRes = R.drawable.ic_done,
                textRes = R.string.search,
                onCustomAction = Runnable {
                    onConfirmPressed()
                }
            )
        )

        return customActionList
    }

    private fun onConfirmPressed() {
        navigator().goBack()
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
        fun newInstance() = DetailMovieFragment()
    }
}