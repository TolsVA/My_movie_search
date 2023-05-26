package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.my_movie_search.model.Trailers

@Entity(
    tableName = "trailers",
    indices = [
        Index("name", unique = true)
    ]
)
data class TrailersDbEntity(
    @ColumnInfo(name = "id_row") @PrimaryKey(autoGenerate = true) var idRow: Long,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String?,
    @ColumnInfo(name = "url") val url: String
) {
    companion object {
        fun fromTrailersData(trailers: Trailers) = TrailersDbEntity(
            idRow = 0,
            name = trailers.name,
            url = trailers.url!!
        )
    }
}