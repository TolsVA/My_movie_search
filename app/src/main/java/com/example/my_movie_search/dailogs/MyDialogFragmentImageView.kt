package com.example.my_movie_search.dailogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.ZoomOutPageTransformer
import com.example.my_movie_search.adapters.MyAdapterIcon

class MyDialogFragmentImageView : DialogFragment() {
    private var pager2: ViewPager2? = null
    var icons: TypedArray? = null
    var resourceId = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val customView = layoutInflater.inflate(R.layout.icon_name_group, null)
//        val editText = customView.findViewById<EditText>(R.id.folder_name)
        icons = resources.obtainTypedArray(R.array.world_cover_landscape)
        pager2 = customView.findViewById(R.id.pager_photo)
        pager2!!.adapter = MyAdapterIcon(this, icons!!)
        pager2!!.setPageTransformer(ZoomOutPageTransformer())
        savePosition()
        return AlertDialog.Builder(requireContext())
            .setView(customView)
            .setPositiveButton(resources.getString(R.string.create_contact)) { _: DialogInterface?, _: Int ->
//                val user = User(resourceId, editText.text.toString())
//                val activity: Activity = requireActivity()
//                (activity as MainActivity).create(user)
            }
            .setNeutralButton(resources.getString(R.string.cancel), null)
            .create()
    }

    private fun savePosition() {
        pager2!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                resourceId = icons!!.getResourceId(position, 0)
            }
        })
    }

    companion object {
//        const val TAG = "MyDialogFragmentImageView"
//        fun newInstance(): MyDialogFragmentImageView {
//            return MyDialogFragmentImageView()
//        }
    }
}