CREATE TABLE public."USER" (
	"ID" uuid NOT NULL,
	"PSEUDO" varchar NOT NULL,
	"PASSWORD" varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY ("ID"),
	CONSTRAINT user_un UNIQUE ("PSEUDO")
);