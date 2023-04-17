package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.databinding.FragmentDetailPersonsBinding
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.viewModel.AppState
import com.example.my_movie_search.viewModel.MainViewModel
import com.squareup.picasso.Picasso

class DetailPersonsFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private var _binding: FragmentDetailPersonsBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPersonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.apply {
            getLiveDataDetailPersons().observe(viewLifecycleOwner) {
                renderData(it)
                viewModel.getPersonsMovie(it)
            }
            getLiveDataMoviesPersons().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }
    }

    private fun renderData(appState: AppState) {

    }


    private fun renderData(persons: Persons) {

        binding.apply {
            Picasso.get()
                .load(persons.photo)
                .into(ivPersonsDetail)


            rvListMovie.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

//            rvListMovie.adapter = adapter.apply {

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
        fun newInstance() = DetailMovieFragment()
    }
}