package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.my_movie_search.model.Genres
import com.example.my_movie_search.model.Movie

@Entity(
    tableName = "movies_genres_settings",
    foreignKeys = [
        ForeignKey(
            entity = MovieDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["movie_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenresDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["genres_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["movie_id_row", "genres_id_row"],
//    indices = [
//        Index("genres_id_row")
//    ]
)
data class MovieGenresSettingsDbEntity(
    @ColumnInfo(name = "movie_id_row") val movieIdRow: Long,
    @ColumnInfo(name = "movies_name") val movieName: String?,
    @ColumnInfo(name = "genres_id_row") val genresIdRow: Long,
    @ColumnInfo(name = "genres_name") val genresName: String?,
) {
    companion object {
        fun fromMovieGenresSettingsDbEntity(
            movie: Movie,
            genres: Genres
        ) = MovieGenresSettingsDbEntity(
            movieIdRow = movie.idRow,
            movieName = movie.name,
            genresIdRow = genres.idRow,
            genresName = genres.name
        )
    }
}