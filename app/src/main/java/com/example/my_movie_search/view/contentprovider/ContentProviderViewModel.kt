package com.example.my_movie_search.view.contentprovider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.provider.Telephony.Sms
import android.telephony.SmsManager
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_search.adapters.AdapterItem
import com.example.my_movie_search.app.App
import com.example.my_movie_search.model.MyContact
import com.example.my_movie_search.view.MainActivity


class ContentProviderViewModel(
    private val liveDataContacts: MutableLiveData<MutableList<AdapterItem>> = MutableLiveData(),
    private val liveDataSMS: MutableLiveData<MutableList<AdapterItem>> = MutableLiveData(),
) : ViewModel() {

    fun getLiveDataContact() = liveDataContacts
    fun getLiveDataSMS() = liveDataSMS

    fun getListSMS(){
        MainActivity.getHandler().post {
            App.appInstance.applicationContext?.let { context ->
                val cursor = context.contentResolver.query(
                    Sms.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

                cursor?.let {
                    val contacts = mutableListOf<AdapterItem>()
                    while (it.moveToNext()) {
                            MyContact(
                                name = it.getString(
                                    it.getColumnIndexOrThrow(
                                        Sms.ADDRESS
                                    )
                                ),
                                photo = null,
                                phone = "",
                                sms = it.getString(
                                    it.getColumnIndexOrThrow(
                                        Sms.BODY
                                    )
                                )
                            ).also { contact ->
                                contacts.add(contact)
                            }
                    }
                    liveDataSMS.postValue(contacts)
                    val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
                    smsManager.sendTextMessage("+89135399801", null, "Да", null, null)

//
//                    val smsIntent = Intent(Intent.ACTION_VIEW)
//                    smsIntent.setType("vnd.android-dir/mms-sms")
//                    smsIntent.putExtra("sms_body", "Some SMS text")
//                    startActivity(smsIntent)

                    val uriString= "https://www.youtube.com/embed/0RqDiYnFxTk"
//                Log.d("MyLog", (movie.videos?.trailers?.get(0)?.name).toString())

                }
                cursor?.close()
            }
        }
    }

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
                                ),
                                sms = null
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