package com.example.my_movie_search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_movie_search.R
import com.example.my_movie_search.databinding.ItemMovieBinding
import com.example.my_movie_search.model.Movie

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    private var movieList = ArrayList<Movie>()

    private var onClickItem: OnClickItem? = null

    private fun getOnClickItem(): OnClickItem? {
        return onClickItem
    }

    fun setOnClickItem(onClickItem: OnClickItem) {
        this.onClickItem = onClickItem
    }

    interface OnClickItem {
        fun onClickItem(bindingItem: ItemMovieBinding, movie: Movie, position: Int)
    }

    fun addMovieList(movieList: ArrayList<Movie>) {
        this.movieList = movieList
    }

    class ItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemMovieBinding.bind(item)
        fun bind(movie: Movie) = with(binding) {
            ivMovie.setImageResource(movie.imageId)
            tvMovie.text = movie.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val view = holder.itemView
        val binding = ItemMovieBinding.bind(view)
        val movie: Movie = movieList[position]
        holder.bind(movie)
        binding.apply {
            view.setOnClickListener {
                if (getOnClickItem() != null) {
                    getOnClickItem()!!.onClickItem(binding, movie, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}