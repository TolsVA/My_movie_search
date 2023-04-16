PRAGMA foreign_keys = ON;

CREATE TABLE "movies" (
	"id_row"	    INTEGER NOT NULL,
	"id"	        INTEGER NOT NULL UNIQUE,
	"name"	        TEXT NOT NULL COLLATE NOCASE,
	"poster_url"	TEXT,
	"rating"	    REAL,
	"movieLength"	INTEGER,
	"type"	        TEXT COLLATE NOCASE,
	"description"	TEXT COLLATE NOCASE,
	"year"	        INTEGER,
	PRIMARY KEY("id_row" AUTOINCREMENT)
);

CREATE TABLE "persons" (
	"id_row"	INTEGER NOT NULL,
	"id"	    INTEGER NOT NULL UNIQUE,
	"photo"	    TEXT,
	"name"	    TEXT NOT NULL COLLATE NOCASE,
	PRIMARY KEY("id_row" AUTOINCREMENT)
);

CREATE TABLE "movies_persons_settings" (
	"movies_id_row"	INTEGER NOT NULL,
	"movies_name"   TEXT NOT NULL,
	"persons_id"    INTEGER NOT NULL,
	"persons_name"	TEXT NOT NULL COLLATE NOCASE,
	FOREIGN KEY("movies_id_row") REFERENCES "movies"("id_row")
	    ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("persons_id") REFERENCES "persons"("id")
	    ON UPDATE CASCADE ON DELETE CASCADE
	UNIQUE("movies_id_row", "persons_id")
);

CREATE TABLE "trailers" (
	"id_row"	INTEGER NOT NULL,
	"url"	    TEXT NOT NULL UNIQUE,
	"name"	    TEXT NOT NULL COLLATE NOCASE,
	PRIMARY KEY("id_row" AUTOINCREMENT)
);

CREATE TABLE "movies_trailers_settings" (
	"movies_id_row"	INTEGER NOT NULL,
	"movies_name"   TEXT NOT NULL,
	"trailers_id_row"    INTEGER NOT NULL,
	"trailers_name"	    TEXT NOT NULL,
	FOREIGN KEY("movies_id_row") REFERENCES "movies"("id_row")
	ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("trailers_id_row") REFERENCES "trailers"("id_row")
	ON UPDATE CASCADE ON DELETE CASCADE
	UNIQUE("movies_id_row", "trailers_id_row")
);

CREATE TABLE "countries" (
	"id_row"	INTEGER NOT NULL,
	"name"	    TEXT NOT NULL UNIQUE COLLATE NOCASE,
	PRIMARY KEY("id_row" AUTOINCREMENT)
);

CREATE TABLE "movies_countries_settings" (
	"movies_id_row"  INTEGER NOT NULL,
	"movies_name"    TEXT NOT NULL,
	"countries_name" TEXT NOT NULL,
	FOREIGN KEY("movies_id_row") REFERENCES "movies"("id_row")
	ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("countries_name") REFERENCES "countries"("name")
	ON UPDATE CASCADE ON DELETE CASCADE
	UNIQUE("movies_id_row", "countries_name")
);

CREATE TABLE "genres" (
	"id_row"	INTEGER NOT NULL,
	"name"	    TEXT NOT NULL UNIQUE COLLATE NOCASE,
	PRIMARY KEY("id_row" AUTOINCREMENT)
);

CREATE TABLE "movies_genres_settings" (
	"movies_id_row"	INTEGER NOT NULL,
	"movies_name"   TEXT NOT NULL,
	"genres_name"	TEXT NOT NULL,
	FOREIGN KEY("movies_id_row") REFERENCES "movies"("id_row")
	ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("genres_name") REFERENCES "genres"("name")
	ON UPDATE CASCADE ON DELETE CASCADE
	UNIQUE("movies_id_row", "genres_name")
);