package com.example.my_movie_search.adapters

import android.content.res.TypedArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.my_movie_search.view.details.PhotoFragment.Companion.newInstance

class MyAdapterIcon(fragment: Fragment, private val icons: TypedArray) :
    FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return newInstance(icons.getResourceId(position, 0))
    }

    override fun getItemCount(): Int {
        return icons.length()
    }
}