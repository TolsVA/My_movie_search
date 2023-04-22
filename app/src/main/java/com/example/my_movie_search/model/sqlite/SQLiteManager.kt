package com.example.my_movie_search.model.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.core.database.sqlite.transaction
import com.example.my_movie_search.model.AuthException
import com.example.my_movie_search.model.Country
import com.example.my_movie_search.model.Genres
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.model.Poster
import com.example.my_movie_search.model.Rating
import com.example.my_movie_search.model.Trailers
import com.example.my_movie_search.model.Videos

class SQLiteManager(private val db: SQLiteDatabase) {

    fun getMoviesFromDb2(filter: String): MutableList<Movie> {
        db.delete(MovieTable.TABLE_NAME, null, null);
        db.delete(PersonsTable.TABLE_NAME, null, null);
        db.delete(MoviePersonsSettingsTable.TABLE_NAME, null, null);
        db.delete(TrailersTable.TABLE_NAME, null, null);
        db.delete(MovieTrailersSettingsTable.TABLE_NAME, null, null);
        db.delete(CountriesTable.TABLE_NAME, null, null);
        db.delete(MovieCountriesSettingsTable.TABLE_NAME, null, null);
        db.delete(GenresTable.TABLE_NAME, null, null);
        db.delete(MovieGenresSettingsTable.TABLE_NAME, null, null);

        return mutableListOf()
    }

    //все фил в баз -> "" или выб по -> filter
    fun getMoviesFromDb(filter: String): MutableList<Movie> {

        val selection = MovieTable.COLUMN_NAME + " = ?"
        val selectionArgs = arrayOf("\"*$filter*\"")
        return getMovie(
            db.query(
                MovieTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            )
        )
    }

    // выб акт по id фил
    private fun getPersonsFromDb(idRow: Long): MutableList<Persons> {

        val sql = "SELECT  ${PersonsTable.TABLE_NAME}.*, ${MoviePersonsSettingsTable.TABLE_NAME}." +
                "${MoviePersonsSettingsTable.COLUMN_MOVIE_NAME} " +
                "FROM ${PersonsTable.TABLE_NAME} " +
                "INNER JOIN  ${MoviePersonsSettingsTable.TABLE_NAME} " +
                "ON  ${PersonsTable.TABLE_NAME}.${PersonsTable.COLUMN_NAME} = " +
                "${MoviePersonsSettingsTable.TABLE_NAME}." +
                "${MoviePersonsSettingsTable.COLUMN_PERSONS_NAME} " +
                "AND ${MoviePersonsSettingsTable.TABLE_NAME}.${MoviePersonsSettingsTable.COLUMN_MOVIE_ID_ROW}= $idRow;"

        val cursor = db.rawQuery(sql, null, null)

        val persons = mutableListOf<Persons>()
        cursor.use {
            if (cursor.count == 0) throw AuthException()

            while (cursor.moveToNext()) {
                persons.add(
                    Persons(
                        cursor.getLong(cursor.getColumnIndexOrThrow(PersonsTable.COLUMN_ID_ROW)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(PersonsTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PersonsTable.COLUMN_PHOTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PersonsTable.COLUMN_NAME)),
                        null,
                        null,
                        null
                    )
                )
            }

        }

        return persons
    }

    private fun getRatingFromDb(): Rating {
        return Rating(
            null,
            null,
            null,
            null,
            null
        )

    }

    // доб фил в баз
    fun insertMovieToDb(movies: MutableList<Movie>) {
        db.transaction {
            for (movie in movies) {
                try {
                    insertOrThrow(
                        MovieTable.TABLE_NAME,
                        null,
                        contentValuesOf(
                            MovieTable.COLUMN_ID to movie.id,
                            MovieTable.COLUMN_NAME to movie.name,
                            MovieTable.COLUMN_POSTER_URL to movie.poster?.url,
                            MovieTable.COLUMN_RATING_KP to movie.rating?.kp,
                            MovieTable.COLUMN_MOVIE_LENGTH to movie.movieLength,
                            MovieTable.COLUMN_TYPE to movie.type,
                            MovieTable.COLUMN_DESCRIPTION to movie.description,
                            MovieTable.COLUMN_YEAR to movie.year
                        ).apply {
                            movie.id_row = insert(
                                MovieTable.TABLE_NAME,
                                null,
                                this
                            )
                        }
                    )
                } catch (e: SQLiteConstraintException) {
//                    val appException = StorageException()
//                    appException.initCause(e)
//                    throw appException
                    Throwable(e)
                }

                for (person in movie.persons) {
                    try {
                        insertOrThrow(
                            PersonsTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                PersonsTable.COLUMN_ID to person.id,
                                PersonsTable.COLUMN_PHOTO to person.photo,
                                PersonsTable.COLUMN_NAME to person.name
                            ).apply {
                                person.id_row = insert(
                                    PersonsTable.TABLE_NAME,
                                    null,
                                    this
                                )
                            }
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }

                    try {
                        insertOrThrow(
                            MoviePersonsSettingsTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                MoviePersonsSettingsTable
                                    .COLUMN_MOVIE_ID_ROW to movie.id_row,
                                MoviePersonsSettingsTable
                                    .COLUMN_MOVIE_NAME to movie.name,
                                MoviePersonsSettingsTable
                                    .COLUMN_PERSONS_ID to person.id,
                                MoviePersonsSettingsTable
                                    .COLUMN_PERSONS_NAME to person.name,
                            )
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }
                }

                if (movie.videos != null) {
                    for (trailer in movie.videos.trailers) {
                        try {
                            insertOrThrow(
                                TrailersTable.TABLE_NAME,
                                null,
                                contentValuesOf(
                                    TrailersTable.COLUMN_URL to trailer.url,
                                    TrailersTable.COLUMN_NAME to trailer.name
                                ).apply {
                                    trailer.id_row = insert(
                                        TrailersTable.TABLE_NAME,
                                        null,
                                        this
                                    )
                                }
                            )
                        } catch (e: SQLiteConstraintException) {
//                            val appException = StorageException()
//                            appException.initCause(e)
//                            throw appException
                            Throwable(e)
                        }

                        try {
                            insertOrThrow(
                                MovieTrailersSettingsTable.TABLE_NAME,
                                null,
                                contentValuesOf(
                                    MovieTrailersSettingsTable
                                        .COLUMN_MOVIE_ID_ROW to movie.id_row,
                                    MovieTrailersSettingsTable
                                        .COLUMN_MOVIE_NAME to movie.name,
                                    MovieTrailersSettingsTable
                                        .COLUMN_TRAILERS_ID_ROW to trailer.id_row,
                                    MovieTrailersSettingsTable
                                        .COLUMN_TRAILERS_NAME to trailer.name,
                                )
                            )
                        } catch (e: SQLiteConstraintException) {
//                            val appException = StorageException()
//                            appException.initCause(e)
//                            throw appException
                            Throwable(e)
                        }
                    }
                }

                for (countries in movie.countries) {
                    try {
                        insertOrThrow(
                            CountriesTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                CountriesTable.COLUMN_NAME to countries.name
                            ).apply {
                                countries.id_row = insert(
                                    CountriesTable.TABLE_NAME,
                                    null,
                                    this
                                )
                            }
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }

                    try {
                        insertOrThrow(
                            MovieCountriesSettingsTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                MovieCountriesSettingsTable
                                    .COLUMN_MOVIE_ID_ROW to movie.id_row,
                                MovieCountriesSettingsTable
                                    .COLUMN_MOVIE_NAME to movie.name,
                                MovieCountriesSettingsTable
                                    .COLUMN_COUNTRIES_NAME to countries.name,
                            )
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }
                }

                for (genre in movie.genres) {
                    try {
                        insertOrThrow(
                            GenresTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                GenresTable.COLUMN_NAME to genre.name
                            ).apply {
                                genre.id_row = insert(
                                    GenresTable.TABLE_NAME,
                                    null,
                                    this
                                )
                            }
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }

                    try {
                        insertOrThrow(
                            MovieGenresSettingsTable.TABLE_NAME,
                            null,
                            contentValuesOf(
                                MovieGenresSettingsTable
                                    .COLUMN_MOVIE_ID_ROW to movie.id_row,
                                MovieGenresSettingsTable
                                    .COLUMN_MOVIE_NAME to movie.name,
                                MovieGenresSettingsTable
                                    .COLUMN_GENRES_NAME to genre.name,
                            )
                        )
                    } catch (e: SQLiteConstraintException) {
//                        val appException = StorageException()
//                        appException.initCause(e)
//                        throw appException
                        Throwable(e)
                    }
                }
            }
        }
    }

    //выб фил по id Акт
    fun getMoviesPersonsFromSQLite(id: Long?): MutableList<Movie> {

        val sql = "SELECT  ${PersonsTable.TABLE_NAME}.*, ${MoviePersonsSettingsTable.TABLE_NAME}." +
                "${MoviePersonsSettingsTable.COLUMN_MOVIE_NAME} " +
                "FROM ${PersonsTable.TABLE_NAME} " +
                "INNER JOIN  ${MoviePersonsSettingsTable.TABLE_NAME} " +
                "ON  ${PersonsTable.TABLE_NAME}.${PersonsTable.COLUMN_NAME} = " +
                "${MoviePersonsSettingsTable.TABLE_NAME}." +
                "${MoviePersonsSettingsTable.COLUMN_PERSONS_NAME} " +
                "AND ${PersonsTable.TABLE_NAME}.${PersonsTable.COLUMN_ID} =$id"

        return getMovie(db.rawQuery(sql, null, null))
    }

    private fun getMovie(cursor: Cursor): MutableList<Movie> {
        val movies = mutableListOf<Movie>()
        cursor.use {
//            Log.d("MyLog", cursor.toString())
            if (cursor.count == 0) movies

            while (cursor.moveToNext()) {
                val idRow = cursor.getLong(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_ID_ROW))

                val ratingKp = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(MovieTable.COLUMN_RATING_KP)
                )

                val posterUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(MovieTable.COLUMN_POSTER_URL)
                )


                val movie = Movie(
                    idRow,
                    Rating(ratingKp, null, null, null, null),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_MOVIE_LENGTH)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MovieTable.COLUMN_YEAR)),
                    Poster(posterUrl, null),
                    listOf(Genres(null, null)),
                    listOf(Country(null, null)),
                    Videos(
                        listOf(
                            Trailers(
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        ),
                        listOf("")
                    ),
                    getPersonsFromDb(idRow)
                )
                movies.add(movie)
            }
        }
        return movies
    }
}