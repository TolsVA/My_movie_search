package com.example.my_movie_search

import android.os.Parcel
import android.os.Parcelable

data class User(var imageId: Int, var title: String) : Parcelable {
    var age: Int

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
        age = parcel.readInt()
    }

    init {
        age = randomAge()
    }

    private fun randomAge(): Int = (0..30).random()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageId)
        parcel.writeString(title)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
