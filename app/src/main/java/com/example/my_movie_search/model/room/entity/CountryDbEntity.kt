package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Movie

@Entity(
    tableName = "country",
    indices = [
        Index("name", unique = true)
    ]
)
data class CountryDbEntity(
    @ColumnInfo(name = "id_row") @PrimaryKey(autoGenerate = true) var idRow: Long,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String,
) {
    fun toCountry(): Country = Country(
        idRow = idRow,
        name = name
    )
    companion object {
        fun fromCountryData(country: Country) = CountryDbEntity(
            idRow = 0,
            name = country.name!!
        )
    }
}