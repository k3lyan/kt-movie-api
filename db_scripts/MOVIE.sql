CREATE TABLE public."MOVIE" (
	"ID" uuid NOT NULL,
	"TITLE" varchar NOT NULL,
	"DIRECTOR" varchar NOT NULL,
	"RELEASEDATE" date NULL,
	"CAST" varchar NULL,
	"GENRE" varchar NULL,
	"SYNOPSIS" varchar NULL,
	"USERID" uuid NOT NULL,
	CONSTRAINT movie_pk PRIMARY KEY ("ID"),
	CONSTRAINT movie_un UNIQUE ("TITLE")
);
