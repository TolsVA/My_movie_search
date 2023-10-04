package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my_movie_search.model.Genres

@Entity(
    tableName = "genres",
//    indices = [
//        Index("name", unique = true)
//    ]
)
data class GenresDbEntity(
    @ColumnInfo(name = "id_row") @PrimaryKey(autoGenerate = true) var idRow: Long,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String?,
) {
    fun toGenres(): Genres = Genres(
        idRow = idRow,
        name = name
    )

    companion object {
        fun fromGenresData(genres: Genres) = GenresDbEntity(
            idRow = 0,
            name = genres.name
        )
    }
}