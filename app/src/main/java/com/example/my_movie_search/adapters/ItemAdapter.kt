package com.example.my_movie_search.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_movie_search.R
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.MyContact
import com.example.my_movie_search.model.Persons
import com.squareup.picasso.Picasso

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_PERSONS = 1
        private const val TYPE_CONTACTS = 2
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

    fun addList(items: MutableList<AdapterItem>) {
        this.items = items
    }

    inner class ItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(item: AdapterItem, position: Int, view: View) {

            val url = when (item) {
                is Movie -> {
                    item.poster?.url
                }

                is Persons -> {
                    item.photo
                }

                is MyContact -> {
                    item.photo
                }

                else -> {
                    ""
                }
            }

            Glide
                .with(view.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(
                    if (getItemViewType(position) == TYPE_MOVIE || getItemViewType(position) == TYPE_PERSONS) {
                        view.rootView.findViewById<ImageView>(R.id.iv_movie_portrait)
                    } else {
                        view.rootView.findViewById<ImageView>(R.id.iv_contact)
                    }
                )

//            Picasso.get()
//                .load(
//                    url
//                )
////                .transform(CircleTransformation())
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(
//                    if (getItemViewType(position) == TYPE_MOVIE || getItemViewType(position) == TYPE_PERSONS) {
//                        view.rootView?.findViewById(R.id.iv_movie_portrait)
//                    } else {
//                        view.rootView?.findViewById(R.id.iv_contact)
//                    }
//                )
            if (item is MyContact) {
                view.rootView.findViewById<TextView>(R.id.textViewTell).text = item.phone
                view.rootView.findViewById<TextView>(R.id.textView3).text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        return ItemHolder(
            item = LayoutInflater
                .from(parent.context)
                .inflate(
                    when (viewType) {
                        TYPE_MOVIE -> {
                            R.layout.item_movie_portrait
                        }

                        TYPE_PERSONS -> {
                            R.layout.item_movie_portrait
                        }

                        TYPE_CONTACTS -> {
                            R.layout.item_my_contacts
                        }

                        else -> {
                            R.layout.item_movie_portrait
                        }
                    },
                    parent,
                    false
                )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Movie) {
            TYPE_MOVIE
        } else if (items[position] is Persons) {
            TYPE_PERSONS
        } else if (items[position] is MyContact) {
            TYPE_CONTACTS
        } else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
        holder.itemView.apply {
            val item = items[position]

            holder.bind(
                item,
                position,
                this.rootView
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