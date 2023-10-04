package com.example.my_movie_search.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_movie_search.R
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.squareup.picasso.Picasso

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_PERSONS = 1
    }

    //    private var movieList: MutableList<Movie> =  mutableListOf()
    private var items: MutableList<AdapterItem> = mutableListOf()
    private var onClickItem: OnClickItem? = null

    private fun getOnClickItem(): OnClickItem? = onClickItem

    fun setOnClickItem(onClickItem: OnClickItem?) {
        this.onClickItem = onClickItem
    }

    interface OnClickItem {
        fun onClickItem(item: AdapterItem, position: Int)
    }

//    fun addMovieList(movieList: MutableList<Movie>) {
//        this.movieList = movieList
//    }

    fun addList(_items: MutableList<AdapterItem>) {
        items = _items
    }

    inner class ItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(item: AdapterItem, imageView: ImageView) {

            val url = when (item) {
                is Movie -> {
                    item.poster?.url
                }
                is Persons -> {
                    item.photo
                }
                else -> {
                    ""
                }
            }

            Picasso.get()
                .load(
                    url
                )
//                .transform(CircleTransformation())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        return ItemHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    if (viewType == TYPE_MOVIE) R.layout.item_movie_portrait else R.layout.item_movie_portrait,
                    parent,
                    false
                )
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is ItemMovie) {
            return TYPE_MOVIE
        }
        return if (items[position] is ItemPersons) {
            TYPE_PERSONS
        } else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
        holder.itemView.apply {
            val item = items[position]
            holder.bind(
                item,
                findViewById(R.id.iv_movie_portrait)
            )

            setOnClickListener {
                getOnClickItem()?.onClickItem(item, position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addPosition(listItem: MutableList<AdapterItem>) {
        for (item in listItem) {
            this.items.add(item)
        }
    }
}