package com.example.my_movie_search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_movie_search.R
import com.example.my_movie_search.model.Movie

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    private lateinit var movieList: MutableList<Movie>
    private var isLocal: Boolean = true
    private var onClickItem: OnClickItem? = null

    private fun getOnClickItem(): OnClickItem? = onClickItem

    fun setOnClickItem(onClickItem: OnClickItem?) {
        this.onClickItem = onClickItem
    }

    interface OnClickItem {
        fun onClickItem(movie: Movie, position: Int)
    }

    fun addMovieList(movieList: MutableList<Movie>) {
        this.movieList = movieList
    }

    inner class ItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(movie: Movie, imageView: ImageView) {
            imageView.setImageResource(movie.imageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    when (isLocal) {
                        true -> R.layout.item_movie_portrait
                        false -> R.layout.item_movie_landscape
                    },
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemView.apply {
            val movie = movieList[position]
            holder.bind(
                movie,
                when (isLocal) {
                    true -> findViewById(R.id.iv_movie_portrait)
                    false -> findViewById(R.id.iv_movie_landscape)
                }
            )

            setOnClickListener {
                getOnClickItem()?.onClickItem(movie, position)
            }
        }
    }

    override fun getItemCount(): Int = movieList.size

    fun setLocation(b: Boolean) {
        isLocal = b
    }
}