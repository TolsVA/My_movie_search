package com.example.my_movie_search.adapters

import com.example.my_movie_search.model.Persons

class ItemPersons(item: Persons) : AdapterItem {
    private val persons: Persons

    init {
        persons = item
    }

    fun getPersons(): Persons {
        return persons
    }
}