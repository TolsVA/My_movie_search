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
    private lateinit var onClickItem: OnClickItem

    private fun getOnClickItem(): OnClickItem = onClickItem

    fun setOnClickItem(onClickItem: OnClickItem) {
        this.onClickItem = onClickItem
    }

    interface OnClickItem {
        fun onClickItem(movie: Movie, position: Int)
    }

    fun addMovieList(movieList: MutableList<Movie>) {
        this.movieList = movieList
    }

    class ItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(movie: Movie, imageView: ImageView?) {
            imageView?.setImageResource(movie.imageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                if (isLocal) {
                    R.layout.item_movie_portrait
                } else {
                    R.layout.item_movie_landscape
                },
                parent,
                false
            )
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val view = holder.itemView
        val imageView: ImageView = if (isLocal) {
            view.findViewById(R.id.iv_movie_portrait)
        } else {
            view.findViewById(R.id.iv_movie_landscape)
        }

        val movie: Movie = movieList[position]
        holder.bind(movie, imageView)

        view.setOnClickListener {
            getOnClickItem().onClickItem(movie, position)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setLocation(b: Boolean) {
        isLocal = b
    }
}