package com.example.my_movie_search.model

data class MovieListPersonsId (
  var docs  : MutableList<Docs>,
)

data class Docs (
  var id     : Long?,
  var movies : MutableList<Movies>
)

data class Movies (
  var id          : Long?,
  var name        : String?,
  var rating      : Double?,
  var general     : Boolean?,
  var description : String?
)