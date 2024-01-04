package com.example.my_movie_search.view.contentprovider

import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.MyContact
import com.example.my_movie_search.view.MainActivity

class ContentProviderViewModel(
    private val liveDataContacts: MutableLiveData<MutableList<AdapterItem>> = MutableLiveData(),
) : ViewModel() {

    fun getLiveDataContact() = liveDataContacts

    fun getListContact() {
        MainActivity.getHandler().post {
            App.appInstance.applicationContext?.let { context ->
                val cursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

                cursor?.let {
                    val contacts = mutableListOf<AdapterItem>()
                    while (it.moveToNext()) {

                        val posPhoto = it.getString(
                            it.getColumnIndexOrThrow(
                                Contacts.PHOTO_URI
                            )
                        )
                        if (posPhoto != null) {
                            MyContact(
                                name = it.getString(
                                    it.getColumnIndexOrThrow(
                                        Contacts.DISPLAY_NAME
                                    )
                                ),
                                photo = posPhoto,
                                phone = it.getString(
                                    it.getColumnIndexOrThrow(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
                            ).also { contact ->
                                contacts.add(contact)
                            }
                        }
                    }
                    liveDataContacts.postValue(contacts)
                }
                cursor?.close()
            }
        }
    }
}