package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Poster
import com.example.my_movie_search.model.Rating

@Entity(
    tableName = "movies",
    indices = [
        Index("id", unique = true)
    ]
)
data class MovieDbEntity(
    @ColumnInfo(name = "id_row") @PrimaryKey(autoGenerate = true) var idRow: Long,
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String,
    @ColumnInfo(name = "poster_url") val posterUrl: String?,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "movie_length") val movieLength: Long?,
    @ColumnInfo(name = "type", collate = ColumnInfo.NOCASE) val type: String?,
    @ColumnInfo(name = "description", collate = ColumnInfo.NOCASE) val description: String?,
    @ColumnInfo(name = "year") val year: Long?,
//    @ColumnInfo(name = "persons") val persons: MutableList<PersonsDbEntity>
) {
    fun toMovie(): Movie = Movie(
        idRow = idRow,
        rating = toRating(),
        movieLength = movieLength,
        id = id,
        type = type,
        name = name,
        description = description,
        year = year,
        poster = toPoster(),
        genres = mutableListOf(), //List<Genres>,
        countries = mutableListOf(), //List<Country>,
        videos = null, //Videos,
        persons = mutableListOf() //MutableList<Persons>
//        persons = (App.getAppDb().getPersonsDao().findByIdMoviePersons(idRow)).map {
//            it.toPersons()
//        } as MutableList<Persons>
    )

    private fun toRating(): Rating = Rating(
        kp = rating,
        imdb = null,
        filmCritics = null,
        russianFilmCritics = null,
        await = null
    )

    private fun toPoster(): Poster = Poster(
        url = posterUrl,
        previewUrl = null
    )

    companion object {
        fun fromMovieData(movie: Movie) = MovieDbEntity(
            idRow = 0, // SQLite автоматически генерирует идентификатор, если idRow = 0
            id = movie.id,
            name = movie.name,
            posterUrl = movie.poster?.url,
            rating = movie.rating?.kp,
            movieLength = movie.movieLength,
            type = movie.type,
            description = movie.description,
            year = movie.year
        )
    }
}