package com.example.my_movie_search.view.contentprovider

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_movie_search.R
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.adapters.ItemAdapter
import com.example.my_movie_search.contract.navigator
import com.example.my_movie_search.databinding.FragmentContentProviderBinding
import com.example.my_movie_search.model.MyContact
import com.google.android.material.button.MaterialButton

class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!

    private val adapter: ItemAdapter by lazy {
        ItemAdapter()
    }

    private lateinit var permission: String

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val contentProviderViewModel: ContentProviderViewModel by lazy {
        ViewModelProvider(requireActivity())[ContentProviderViewModel::class.java]
    }

    companion object {
        const val TAG = "ContentProviderFragment"
        private const val ARG_PERMISSION = "ARG_PERMISSION"

        @JvmStatic
        fun newInstance(permission: String) =
            ContentProviderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PERMISSION, permission)
                }
            }
    }

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
        permission = arguments?.getString(ARG_PERMISSION).toString()

        contentProviderViewModel.apply {
            getLiveDataContact().observe(viewLifecycleOwner) {
                setData(it)
            }
        }
        registerPermissionListener()
        checkPermission()
    }

    private fun permissionName(permission: String) =
        when (permission) {
            Manifest.permission.READ_CONTACTS -> {
                getString(R.string.as_contacts)
            }

            Manifest.permission.SEND_SMS -> {
                getString(R.string.sms)
            }

            else -> {
                ""
            }
        }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    when (permission) {
                        Manifest.permission.READ_CONTACTS -> {
                            contentProviderViewModel.getListContact()
                        }

                        Manifest.permission.SEND_SMS -> {
//                            getSMS()
                            Toast.makeText(
                                context,
                                "Разрешение SMS получено",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {
                            ""
                        }
                    }
                }

                shouldShowRequestPermissionRationale(permission) ->
                    AlertDialog.Builder(it)
                        .setTitle(
                            "${getString(R.string.not_have_access)} " +
                                    permissionName(permission)
                        )
                        .setMessage(
                            when (permission) {
                                Manifest.permission.READ_CONTACTS -> getString(
                                    R.string.contacts_click_continue
                                )

                                Manifest.permission.SEND_SMS -> getString(
                                    R.string.sms_click_continue
                                )

                                else -> {
                                    ""
                                }
                            }
                        )
                        .setPositiveButton(getString(R.string.go_on)) { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton(getString(R.string.cancellation)) { dialog, _ ->
                            addView(
                                requireContext(),
                                getTextNegative(permission)
                            )
                            dialog.dismiss()
                        }
                        .create()
                        .show()

                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun getTextNegative(permission: String) =
        "${getString(R.string.not_have_access)} ${
            when (permission) {
                Manifest.permission.READ_CONTACTS -> {
                    getString(R.string.as_contacts)
                }

                Manifest.permission.SEND_SMS -> {
                    getString(R.string.sms)
                }

                else -> {
                    ""
                }
            }
        } \n${requireContext().getString(R.string.one_try)} " +
                requireContext().getString(R.string.change_application_settings)

    private fun registerPermissionListener() {
        permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                when {
                    it[Manifest.permission.READ_CONTACTS] == true -> {
                        contentProviderViewModel.getListContact()
                    }

                    it[Manifest.permission.SEND_SMS] == true -> {
//                        getSMS()
                        Toast.makeText(
                            context,
                            "Разрешение SMS получено",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    !shouldShowRequestPermissionRationale(permission) -> {
                        AlertDialog.Builder(context)
                            .setTitle(
                                "${getString(R.string.not_have_access)} " +
                                        permissionName(permission)
                            )
                            .setMessage(
                                requireContext().getString(R.string.change_application_settings)
                            )
                            .setPositiveButton(getString(R.string.close)) { _, _ ->
                                navigator().goBack()
                            }
                            .create()
                            .show()
                        binding.flContainer.removeAllViews()
                    }

                    else -> {
                        addView(
                            requireContext(),
                            getTextNegative(permission)
                        )
                    }
                }
            }
    }

    private fun requestPermission() {
        permissionLauncher.launch(arrayOf(permission))
    }

    private fun setData(adapterItems: MutableList<AdapterItem>) {
        binding.apply {
            rvListContacts.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            rvListContacts.adapter = adapter.apply {
                addPosition(adapterItems)
                setOnClickItem(object : ItemAdapter.OnClickItem {
                    override fun onClickItem(
                        item: AdapterItem,
                        position: Int
                    ) {
                        startActivity(
                            Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:${(item as MyContact).phone}")
                            )
                        )
                    }
                })
            }
        }
    }

    private fun addView(context: Context, textToShow: String) {
        binding.flContainer.addView(LinearLayoutCompat(context).also { layout ->
            layout.orientation = LinearLayoutCompat.VERTICAL

            layout.addView(AppCompatTextView(context).apply {
                text = textToShow
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })

            layout.addView(MaterialButton(context).apply {
                text = context.getString(R.string.take_advantage)
                textSize = resources.getDimension(R.dimen.main_container_text_size)
                setOnClickListener {
                    layout.removeAllViews()
                    requestPermission()
                }
            })
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}