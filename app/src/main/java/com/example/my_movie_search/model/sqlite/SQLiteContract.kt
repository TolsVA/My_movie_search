package com.example.my_movie_search.model.sqlite

    object MovieTable {
        const val TABLE_NAME = "movies"
        const val COLUMN_ID_ROW = "id_row"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_POSTER_URL = "poster_url"
        const val COLUMN_RATING_KP = "rating"
        const val COLUMN_MOVIE_LENGTH = "movieLength"
        const val COLUMN_TYPE = "type"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_YEAR = "year"
    }

    object PersonsTable {
        const val TABLE_NAME = "persons"
        const val COLUMN_ID_ROW = "id_row"
        const val COLUMN_ID = "id"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_NAME = "name"
    }

    object MoviePersonsSettingsTable {
        const val TABLE_NAME = "movies_persons_settings"
        const val COLUMN_MOVIE_ID_ROW = "movies_id_row"
        const val COLUMN_MOVIE_NAME = "movies_name"
        const val COLUMN_PERSONS_ID = "persons_id"
        const val COLUMN_PERSONS_NAME = "persons_name"
    }

    object TrailersTable {
        const val TABLE_NAME = "trailers"
        const val COLUMN_ID_ROW = "id_row"
        const val COLUMN_URL = "url"
        const val COLUMN_NAME = "name"
    }

    object MovieTrailersSettingsTable {
        const val TABLE_NAME = "movies_trailers_settings"
        const val COLUMN_MOVIE_ID_ROW = "movies_id_row"
        const val COLUMN_MOVIE_NAME = "movies_name"
        const val COLUMN_TRAILERS_ID_ROW = "trailers_id_row"
        const val COLUMN_TRAILERS_NAME = "trailers_name"
    }

    object CountriesTable {
        const val TABLE_NAME = "countries"
        const val COLUMN_ID_ROW = "id_row"
        const val COLUMN_NAME = "name"
    }

    object MovieCountriesSettingsTable {
        const val TABLE_NAME = "movies_countries_settings"
        const val COLUMN_MOVIE_ID_ROW = "movies_id_row"
        const val COLUMN_MOVIE_NAME = "movies_name"
        const val COLUMN_COUNTRIES_NAME = "countries_name"
    }

    object GenresTable {
        const val TABLE_NAME = "genres"
        const val COLUMN_ID_ROW = "id_row"
        const val COLUMN_NAME = "name"
    }

    object MovieGenresSettingsTable {
        const val TABLE_NAME = "movies_genres_settings"
        const val COLUMN_MOVIE_ID_ROW = "movies_id_row"
        const val COLUMN_MOVIE_NAME = "movies_name"
        const val COLUMN_GENRES_NAME = "genres_name"
    }

