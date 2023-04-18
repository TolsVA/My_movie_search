package com.example.my_movie_search.model

import android.os.Parcelable
import com.example.my_movie_search.adapters.AdapterItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieList (
  @SerializedName("docs" )
  val movies  : MutableList<Movie>,
  val total : Int?,
  val limit : Int?,
  val page  : Int?,
  val pages : Int?
): Parcelable

@Parcelize
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
): Parcelable, AdapterItem

@Parcelize
data class Country (
  var id_row : Long?,
  val name   : String?
): Parcelable

@Parcelize
data class Genres (
  var id_row : Long?,
  val name   : String?
): Parcelable

@Parcelize
data class Persons (
  var id_row       : Long?,
  val id           : Long?,
  val photo        : String?,
  val name         : String?,
  val enName       : String?,
  val profession   : String?,
  val enProfession : String?
): Parcelable, AdapterItem

@Parcelize
data class Poster (
  val url        : String?,
  val previewUrl : String?
): Parcelable

@Parcelize
data class Rating (
  val kp                 : Double?,
  val imdb               : Double?,
  val filmCritics        : Double?,
  val russianFilmCritics : Double?,
  val await              : Double?
): Parcelable

@Parcelize
data class Trailers (
  var id_row : Long?,
  val url    : String?,
  val name   : String?,
  val site   : String?,
  val type   : String?
): Parcelable

@Parcelize
data class Videos (
  val trailers : List<Trailers>,
  val teasers  : List<String>
): Parcelable