package com.example.my_movie_search.view.contentprovider

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentContentProviderBinding
import com.example.my_movie_search.model.MyContact

@Suppress("NAME_SHADOWING")
class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(
            inflater, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPermissionListener()
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage(
                            "Для вывода контактов на экран приложению требуется доступ " +
                                    "к контактам для получения доступа нажмите --> ПРОДОЛЖИТЬ"
                        )
                        .setPositiveButton("ПРОДОЛЖИТЬ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("ОТМЕНА") { dialog, _ ->
                            addView(
                                requireContext(),
                                "У приложения нет доступа к контактам \n " +
                                        "У вас осталась одна попытка получить доступ через приложение.\n" +
                                        "В последствии Вы можете изменить состояние доступа \n" +
                                        "через настройки приложения"
                            )
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }

                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun registerPermissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                when {
                    it[Manifest.permission.READ_CONTACTS] == true -> {
                        getContacts()
                    }

                    !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                        AlertDialog.Builder(context)
                            .setTitle("У приложения нет доступа к контактам")
                            .setMessage("Вы можете изменить состояние доступа через настройки приложения")
                            .setPositiveButton("Закрыть") { _, _ ->
                                navigator().goBack()
                            }
                            .create()
                            .show()
                        binding.flContainer.removeAllViews()
                    }

                    else -> {
                        addView(
                            requireContext(),
                            "У приложения нет доступа к контактам \n " +
                                    "У вас осталась одна попытка получить доступ через приложение.\n" +
                                    "В последствии Вы можете изменить состояние доступа \n" +
                                    "через настройки приложения"
                        )
                    }
                }
            }
    }

    private fun requestPermission() {
        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS))
    }

    private fun getContacts() {
//        binding.flContainer.removeAllViews()
//        Toast.makeText(context, "Разрешение получено", Toast.LENGTH_LONG).show()

//        MainActivity.getHandler().post {
        context?.let {
            val cursor = it.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            cursor?.let { cursor ->
                val contacts = mutableListOf<AdapterItem>()
                while (cursor.moveToNext()) {

                    val posPhoto = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            ContactsContract.Contacts.PHOTO_URI
                        )
                    )
                    if (posPhoto != null) {
                        MyContact(
                            name = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    ContactsContract.Contacts.DISPLAY_NAME
                                )
                            ),
                            photo = posPhoto,
                            phone = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                        ).also { it ->
                            contacts.add(it)
                        }
                    }
                }
                setData(contacts)
            }
            cursor?.close()
//            }
        }
    }

    private fun setData(adapterItems: MutableList<AdapterItem>) {
        binding.apply {
            rvListContacts.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            rvListContacts.adapter = adapter.apply {

//                val listItem: MutableList<AdapterItem> = mutableListOf()
//
//                for (myContact in adapterItems) {
//                    listItem.add(myContact)
//                }

                addPosition(adapterItems)
                setOnClickItem(object : ItemAdapter.OnClickItem {
                    override fun onClickItem(
                        item: AdapterItem,
                        position: Int
                    ) {
                        (item as MyContact).let {
//                            navigator().showDetailMovieScreen(it, DetailMovieFragment.TAG)
                            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()

                            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phone}")))
                        }
                    }
                })
            }
        }
    }

    private fun addView(context: Context, textToShow: String) {
        binding.flContainer.addView(LinearLayoutCompat(context).apply {
            orientation = LinearLayoutCompat.VERTICAL

            addView(AppCompatTextView(context).apply {
                text = textToShow
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })

//            addView(MaterialButton(context).apply {
//                text = "Воспользоваться"
//                textSize = resources.getDimension(R.dimen.main_container_text_size)
//                setOnClickListener {
//                    requestPermission()
//                }
//            })
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ContentProviderFragment"

        @JvmStatic
        fun newInstance() =
            ContentProviderFragment()
    }
}