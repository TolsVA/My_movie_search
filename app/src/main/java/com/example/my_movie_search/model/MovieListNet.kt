package com.example.my_movie_search.model

data class MovieListNet (
    val docs: MutableList<Movie>
)

data class Movie (
    val externalID: ExternalID?,
    val rating: Rating?,
    val votes: Rating?,
    val movieLength: Long?,
    val id: Long?,
    val type: Type?,
    val name: String?,
    val description: String?,
    val year: Long?,
    val poster: Poster?,
    val genres: List<Country>,
    val countries: List<Country>,
    val shortDescription: String?,
)

data class Country (
    val name: String?
)

data class ExternalID (
    val kpHD: String?,
    val imdb: String?,
    val tmdb: Long?
)

data class Poster (
    val url: String?,
    val previewUrl: String?
)

data class Rating (
    val kp: Double?,
    val imdb: Double?,
    val filmCritics: Double?,
    val russianFilmCritics: Double?,
    val await: Double?
)

enum class Type(val value: String) {
    Cartoon("cartoon"),
    Movie("movie");

    companion object {
        public fun fromValue(value: String): Type = when (value) {
            "cartoon" -> Cartoon
            "movie"   -> Movie
            else      -> throw IllegalArgumentException()
        }
    }
}
