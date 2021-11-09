CREATE TABLE public."FAVOURITE" (
	"ID" uuid NOT NULL,
	"TITLE" varchar NOT NULL,
	"USERID" uuid NOT NULL,
	CONSTRAINT favourite_pk PRIMARY KEY ("ID"),
	CONSTRAINT favourite_un UNIQUE ("TITLE")
);
