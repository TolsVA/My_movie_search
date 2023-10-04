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
import com.example.my_movie_search.contract.HasCustomActionBottomNavigation
import com.example.my_movie_search.contract.HasCustomTitle
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentDetailMovieBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.utils.mySetText
import com.example.my_movie_search.view.contentprovider.ContentProviderFragment
import com.squareup.picasso.Picasso

class DetailMovieFragment : Fragment(), HasCustomTitle, HasCustomAction, HasCustomActionBottomNavigation {

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
//        detailMovieViewModel.setMovie(movie)
//
//        detailMovieViewModel.apply {
//            getLiveDataDetail().observe(viewLifecycleOwner) {
//                movie = it
//                renderData(movie)
//            }
//        }

        renderData(movie)
    }

    override fun getTitleRes(): Int = R.string.detailing

    override fun getTitle() = movie.type.toString()

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

    override fun getCustomActionBottomNavigation(): MutableList<CustomAction> {
        val customActionList = mutableListOf<CustomAction>()

        customActionList.add(
            CustomAction(
                iconRes = android.R.drawable.sym_action_call,
                textRes = R.string.call,
                onCustomAction = Runnable {
                    Toast.makeText(requireContext(), resources.getString(R.string.call), Toast.LENGTH_SHORT).show()
                }
            )
        )

        customActionList.add(
            CustomAction(
                iconRes = R.drawable.baseline_message_24,
                textRes = R.string.message,
                onCustomAction = Runnable {
                    Toast.makeText(requireContext(), resources.getString(R.string.message), Toast.LENGTH_SHORT).show()
                }
            )
        )

        customActionList.add(
            CustomAction(
                iconRes = R.drawable.outline_contact_phone_24,
                textRes = R.string.contacts,
                onCustomAction = Runnable {
//                    Toast.makeText(requireContext(), resources.getString(R.string.contacts), Toast.LENGTH_SHORT).show()

                    navigator().showContentProviderFragment(ContentProviderFragment.TAG)
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
                        (item as Persons).let {
                            navigator().showDetailPersonsScreen(it, DetailPersonsFragment.TAG)
                        }
                    }
                })
            }

            tvName.mySetText(movie.name)
            tvRating.mySetText(movie.rating?.kp.toString())
            tvType.mySetText(movie.type.toString())
            tvYear.mySetText(movie.year.toString())
            tvLength.mySetText(
                "${movie.movieLength?.div(60)}ч ${movie.movieLength?.rem(60)}м"
            )

            movie.genres.forEach {
                tvGenres.append("${it.name} ")
            }

            movie.countries.forEach {
                tvCountries.append(" ${it.name} ")
            }

            tvDescription.mySetText(movie.description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "DetailMovieFragment"
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