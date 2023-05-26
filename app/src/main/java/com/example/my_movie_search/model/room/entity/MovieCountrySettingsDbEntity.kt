package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Movie

@Entity(
    tableName = "movies_country_settings",
    foreignKeys = [
        ForeignKey(
            entity = MovieDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["movie_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CountryDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["country_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["movie_id_row", "country_id_row"],
    indices = [
        Index("country_id_row")
    ]
)
data class MovieCountrySettingsDbEntity(
    @ColumnInfo(name = "movie_id_row") val movieIdRow: Long,
    @ColumnInfo(name = "movies_name") val movieName: String,
    @ColumnInfo(name = "country_id_row") val countryIdRow: Long,
    @ColumnInfo(name = "country_name") val countryName: String,
) {
    companion object {
        fun fromMovieCountrySettingsDbEntity(
            movie: Movie,
            country: Country
        ) = MovieCountrySettingsDbEntity(
            movieIdRow = movie.idRow,
            movieName = movie.name,
            countryIdRow = country.idRow,
            countryName = country.name!!
        )
    }
}