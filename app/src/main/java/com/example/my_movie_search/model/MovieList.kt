package com.example.my_movie_search.model

import android.os.Parcelable
import com.example.my_movie_search.adapters.AdapterItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//data class MovieListPersonsId (
//  var docs  : MutableList<Docs>
//)
//
//data class Docs (
//  var id     : Long?,
//  var movies : MutableList<Movie>
//)

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
  var idRow       : Long,
  val rating      : Rating?,
  val movieLength : Long?,
  val id          : Long,
  val type        : String?,
  val name        : String,
  val description : String?,
  val year        : Long?,
  val poster      : Poster?,
  var genres      : List<Genres>,
  var countries   : List<Country>,
  val videos      : Videos?,
  var persons     : List<Persons>
): Parcelable, AdapterItem

@Parcelize
data class Country (
  var idRow : Long,
  val name   : String?
): Parcelable

@Parcelize
data class Genres (
  var idRow : Long,
  val name   : String?
): Parcelable

@Parcelize
data class Persons (
  var idRow       : Long,
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
  var idRow : Long,
  val url    : String?,
  val name   : String?,
  val site   : String?,
  val type   : String?
): Parcelable

@Parcelize
data class Videos (
  val trailers : MutableList<Trailers>,
  val teasers  : MutableList<String>
): Parcelable