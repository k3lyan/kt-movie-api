CREATE TABLE public."USER" (
	"ID" uuid NOT NULL,
	"PSEUDO" varchar NOT NULL,
	"PASSWORD" varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY ("ID"),
	CONSTRAINT user_un UNIQUE ("PSEUDO")
);

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

CREATE TABLE public."FAVOURITE" (
	"ID" uuid NOT NULL,
	"TITLE" varchar NOT NULL,
	"USERID" uuid NOT NULL,
	CONSTRAINT favourite_pk PRIMARY KEY ("ID"),
	CONSTRAINT favourite_un UNIQUE ("TITLE")
);
