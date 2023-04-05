package com.example.my_movie_search.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_movie_search.R
import com.example.my_movie_search.model.Movie
import com.squareup.picasso.Picasso

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    private var movieList: MutableList<Movie> =  mutableListOf()
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
        private val context: Context = item.context
        fun bind(movie: Movie, imageView: ImageView) {

            Picasso.with(context)
                .load( movie.poster?.url )
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_movie_portrait,
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
                findViewById(R.id.iv_movie_portrait)
            )

            setOnClickListener {
                getOnClickItem()?.onClickItem(movie, position)
            }
        }
    }

    override fun getItemCount(): Int = movieList.size

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        movieList.clear()
        notifyDataSetChanged()
    }
}