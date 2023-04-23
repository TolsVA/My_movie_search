package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

//    private val detailMovieViewModel: DetailMovieViewModel by lazy {
//        ViewModelProvider(requireActivity())[DetailMovieViewModel::class.java]
//    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var _binding: FragmentDetailMovieBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        movie = savedInstanceState?.getParcelable(ARG_MOVIE)
            ?:arguments?.getParcelable(KEY_MOVIE)!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_MOVIE, movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val observer = Observer<Movie> { movie ->
//            renderData(movie)
//        }
//        detailMovieViewModel.getLiveDataDetail().observe(viewLifecycleOwner, observer)
        renderData(movie)
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


            movie.name?.split(' ')
                ?.filter { it.isNotBlank() }
                ?.forEach {
                    tvName.append("$it\n")
                }

//            tvName.mySetText(movie.name)
            tvRating.mySetText(movie.rating?.kp.toString())
            tvType.mySetText(movie.type.toString())
            tvYear.mySetText(movie.year.toString())
            tvLength.mySetText(
                "${movie.movieLength?.div(60)}ч ${movie.movieLength?.rem(60)}м"
            )


            movie.genres.forEach {
                tvGenres.append("${it.name}\n")
            }

            movie.countries.forEach {
                tvCountries.append(" ${it.name}\n")
            }

            tvDescription.mySetText(movie.description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        private const val KEY_MOVIE = "KEY_MOVIE"

        @JvmStatic
        fun newInstance(movie: Movie) =
            DetailMovieFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MOVIE, movie)
                }
            }
    }
}