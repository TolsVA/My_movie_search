package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons

@Entity(
    tableName = "movies_persons_settings",
    foreignKeys = [
        ForeignKey(
            entity = MovieDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["movie_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonsDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["persons_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["movie_id_row", "persons_id_row"],
//    indices = [
//        Index("persons_id_row")
//    ]
)
data class MoviePersonsSettingsDbEntity(
    @ColumnInfo(name = "movie_id_row") val movieIdRow: Long,
    @ColumnInfo(name = "movies_name") val movieName: String?,
    @ColumnInfo(name = "persons_id_row") val personsIdRow: Long,
    @ColumnInfo(name = "persons_id") val personsId: Long?,
    @ColumnInfo(name = "persons_name") val personsName: String?
) {
    companion object {
        fun fromMoviePersonsSettingsDbEntity(
            movie: Movie,
            persons: Persons
        ) = MoviePersonsSettingsDbEntity(
            movieIdRow = movie.idRow,
            movieName = movie.name,
            personsIdRow = persons.idRow,
            personsName = persons.name,
            personsId = persons.id
        )
    }
}