package com.example.my_movie_search.model.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.core.database.sqlite.transaction
import com.example.my_movie_search.model.*

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
        return getMovie(
            db.query(
                MovieTable.TABLE_NAME,
                null,
                null,
                null,
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
            Log.d("MyLog", cursor.toString())
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


//    public void upgradeEntry(Note note) {
//        ContentValues cv = new ContentValues();
//        cv.put( Constants.FeedEntryNote.NOTE_ID, note.getId());
//        cv.put( Constants.FeedEntryNote.NOTE_TITLE, note.getTitle());
//        cv.put( Constants.FeedEntryNote.NOTE_CONTENT, note.getText());
//        cv.put( Constants.FeedEntryNote.NOTE_DATA, note.getData());
//        cv.put( Constants.FeedEntryNote.NOTE_GROUP_ID, note.getGroup_id ());
//        String where = Constants.FeedEntryNote.NOTE_ID + "=" + note.getId();
//        db.update( Constants.FeedEntryNote.TABLE_NOTE, cv, where, null);
//    }
//
//    public void deleteEntry(long index) {
//        db.delete( Constants.FeedEntryNote.TABLE_NOTE, Constants.FeedEntryNote.NOTE_ID
//                + " = " + index, null);
//    }
//
//    public List<Note> getFromDb(long group_id){
//        String selection;
//        String[] selectionArgs;
//        if (group_id == 0) {
//            selection = null;
//            selectionArgs = null;
//        } else {
//            selection = Constants.FeedEntryNote.NOTE_GROUP_ID + " = ?";
//            selectionArgs = new String[]{String.valueOf ( group_id )};
//        }
//        Cursor cursor = db.query(
//                Constants.FeedEntryNote.TABLE_NOTE,    // Таблица для запроса
//                null,           // Массив возвращаемых столбцов
//                selection,          // Столбцы для предложения WHERE
//                selectionArgs,       // значения для предложения WHERE
//                null,          // не группировать строки
//                null,            // не фильтровать по группам строк
//                sortOrder          // Порядок сортировки
//        );
//
//        List<Note> notes = new ArrayList<>();
//        while(cursor.moveToNext()) {
//
//            long index = cursor.getLong(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_ID));
//
//            String title = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_TITLE));
//
//            String text = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_CONTENT));
//
//            String data = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_DATA));
//
//            long folder_group_id = cursor.getLong (
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_GROUP_ID));
//
//            Note note = new Note(index, title, text, data, folder_group_id);
//            notes.add(note);
//        }
//        cursor.close();
//        return notes;
//    }
//
//    public void clearDb() {
//        db.delete( Constants.FeedEntryNote.TABLE_NOTE, null, null);
//        db.delete( Constants.FeedEntryGroup.TABLE_GROUP, null, null);
//    }
//
//
//    public void closeDb() {
//        dbHelper.close();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public List<Note> searchDb(String _text) {
//
//        String sql = "SELECT * FROM " + Constants.FeedEntryNote.TABLE_NOTE + " WHERE " +
//                Constants.FeedEntryNote.NOTE_TITLE + " GLOB " + "\"*" +  _text + "*\"" +
//                " OR " + Constants.FeedEntryNote.NOTE_CONTENT + " GLOB " + "\"*" +  _text + "*\"";
//
//        Cursor cursor = db.rawQuery ( sql, null, null );
//
//        List<Note> notes = new ArrayList<>();
//        while(cursor.moveToNext ()) {
//
//            long index = cursor.getLong(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_ID));
//
//            String title = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_TITLE));
//
//            String text = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_CONTENT));
//
//            String data = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_DATA));
//
//            Note note = new Note(index, title, text, data, 1);
//            notes.add(note);
//        }
//        cursor.close();
//
//        return notes;
//    }

//    public List<Group> getFromDbGroup(){
//        String sql = "SELECT " + Constants.FeedEntryGroup.TABLE_GROUP + ".*, " +
//                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + ", " +
//                "COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP +
//                " LEFT JOIN " + Constants.FeedEntryNote.TABLE_NOTE +
//                " ON " + Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + " = " +
//                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_ID +
//                " GROUP BY " + Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + ";";
//
//        Cursor cursor = db.rawQuery ( sql, null, null );
//
//        List<Group> groups = new ArrayList<>();
//        while(cursor.moveToNext()) {
//
//            long id = cursor.getLong(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));
//
//            String name = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));
//
//            int icon = cursor.getInt (
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));
//
//            int note_group_id = cursor.getInt (
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_GROUP_ID));
//
//            int count = cursor.getInt (
//                    cursor.getColumnIndexOrThrow( "count" ));
//
//            if (note_group_id == 0) {
//                count = 0;
//            }
//
//            Group group = new Group (id, name, icon, count);
//            groups.add(group);
//        }
//        cursor.close();
//        return groups;
//    }
//
//    public int checkGroupForDbGroup(String _text) {
//
//        String sql = "SELECT COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP + " WHERE " +
//                Constants.FeedEntryGroup.GROUP_NAME + " = \"" + _text + "\"";
//
//        Cursor cursor = db.rawQuery ( sql, null, null );
//
//        int index = 0;
//        while(cursor.moveToNext ()) {
//            index = cursor.getInt (
//                    cursor.getColumnIndexOrThrow ( "count" ));
//        }
//        cursor.close();
//
//        return index;
//    }
//
//
//    public List<Group> searchByGroupNameDbGroup(String folderName) {
//
//        String sql = "SELECT " + Constants.FeedEntryGroup.TABLE_GROUP + ".*, " +
//                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + ", " +
//                "COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP +
//                " INNER JOIN " + Constants.FeedEntryNote.TABLE_NOTE + " ON " +
//                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + " = " +
//                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_ID + " AND " +
//                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + " = '" + folderName + "' " +
//                " GROUP BY " + Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + ";";
//
//        Cursor cursor = db.rawQuery ( sql, null, null );
///*
//        String selection = Constants.FeedEntryGroup.GROUP_NAME + " = ?";
//        String[] selectionArgs = { folderName };
//        Cursor cursor = db.query(
//                Constants.FeedEntryGroup.TABLE_GROUP,    // Таблица для запроса
//                null,           // Массив возвращаемых столбцов
//                selection ,          // Столбцы для предложения WHERE
//                selectionArgs,       // значения для предложения WHERE
//                null,          // не группировать строки
//                null,            // не фильтровать по группам строк
//                null         // Порядок сортировки
//        );*/
//
//        List<Group> groups = new ArrayList<>();
//        while(cursor.moveToNext()) {
//
//            long id = cursor.getLong(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));
//
//            String name = cursor.getString(
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));
//
//            int icon = cursor.getInt (
//                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));
//
//            int count = cursor.getInt (
//                    cursor.getColumnIndexOrThrow( "count" ));
//
//            Group group = new Group (id, name, icon, count);
//            groups.add(group);
//        }
//        cursor.close();
//        return groups;
//
//    }
//
//    public void deleteEntryGroup(long index) {
//        db.delete( Constants.FeedEntryGroup.TABLE_GROUP, Constants.FeedEntryGroup.GROUP_ID
//                + " = " + index, null);
//    }
//
//    public void deleteIndexNoteGroupId(long position) {
//        db.delete( Constants.FeedEntryNote.TABLE_NOTE, Constants.FeedEntryNote.NOTE_GROUP_ID
//                + " = " + position, null);
//    }
