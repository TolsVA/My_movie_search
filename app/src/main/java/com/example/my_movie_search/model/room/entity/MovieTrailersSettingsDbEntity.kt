package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Trailers

@Entity(
    tableName = "movies_trailers_settings",
    foreignKeys = [
        ForeignKey(
            entity = MovieDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["movie_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrailersDbEntity::class,
            parentColumns = ["id_row"],
            childColumns = ["trailers_id_row"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["movie_id_row", "trailers_id_row"],
//    indices = [
//        Index("trailers_id_row")
//    ]
)
data class MovieTrailersSettingsDbEntity(
    @ColumnInfo(name = "movie_id_row") val movieIdRow: Long,
    @ColumnInfo(name = "movies_name") val movieName: String?,
    @ColumnInfo(name = "trailers_id_row") val trailersIdRow: Long,
    @ColumnInfo(name = "trailers_url") val trailersUrl: String?,
) {
    companion object {
        fun fromMovieTrailersSettingsDbEntity(
            movie: Movie,
            trailer: Trailers
        ) = MovieTrailersSettingsDbEntity(
            movieIdRow = movie.idRow,
            movieName = movie.name,
            trailersIdRow = trailer.idRow,
            trailersUrl = trailer.url
        )
    }
}