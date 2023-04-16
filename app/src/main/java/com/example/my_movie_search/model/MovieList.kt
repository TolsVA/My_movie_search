package com.example.my_movie_search.model

import com.example.my_movie_search.adapters.AdapterItem
import com.google.gson.annotations.SerializedName

data class MovieList (
  @SerializedName("docs" )
  val movies  : MutableList<Movie>,
  val total : Int?,
  val limit : Int?,
  val page  : Int?,
  val pages : Int?
)

data class Movie (
  var id_row      : Long?,
  val rating      : Rating?,
  val movieLength : Long?,
  val id          : Long?,
  val type        : String?,
  val name        : String?,
  val description : String?,
  val year        : Long?,
  val poster      : Poster?,
  val genres      : List<Genres>,
  val countries   : List<Country>,
  val videos      : Videos?,
  val persons     : MutableList<Persons>
) : AdapterItem

data class Country (
  var id_row : Long?,
  val name   : String?
)

data class Genres (
  var id_row : Long?,
  val name   : String?
)

data class Persons (
  var id_row       : Long?,
  val id           : Long?,
  val photo        : String?,
  val name         : String?,
  val enName       : String?,
  val profession   : String?,
  val enProfession : String?
) : AdapterItem

data class Poster (
  val url        : String?,
  val previewUrl : String?
)

data class Rating (
  val kp                 : Double?,
  val imdb               : Double?,
  val filmCritics        : Double?,
  val russianFilmCritics : Double?,
  val await              : Double?
)

data class Trailers (
  var id_row : Long?,
  val url    : String?,
  val name   : String?,
  val site   : String?,
  val type   : String?
)

data class Videos (
  val trailers : List<Trailers>,
  val teasers  : List<String>
)