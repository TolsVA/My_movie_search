package com.example.my_movie_search.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.my_movie_search.R

class PhotoFragment : Fragment() {
    private var iv: ImageView? = null
    private var icon = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            icon = requireArguments().getInt(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv = view.findViewById(R.id.image_view_photo)
        iv!!.setImageResource(icon)
    }

    companion object {
        private const val ARG_IMAGE = "ARG_IMAGE"

        @JvmStatic
        fun newInstance(icon: Int): PhotoFragment {
            val fragment = PhotoFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE, icon)
            fragment.arguments = args
            return fragment
        }
    }
}