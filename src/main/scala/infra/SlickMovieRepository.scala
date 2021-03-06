package infra

import cats.implicits.catsSyntaxEitherId
import core.{Movie, MovieDataError, MovieFilters, MovieId, MovieInput, MovieRepository, UniqueMovieViolation, UserId}
import org.postgresql.util.PSQLException
import slick.dbio.DBIO
import infra.MovieRegistryPostgresProfile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class SlickMovieRepository(implicit val ec: ExecutionContext) extends MovieRepository[DBIO] {

  def create(userId: UserId, input: MovieInput): DBIO[Either[MovieDataError, Movie]] = {
    sql"""
      INSERT INTO "MOVIE" ("ID", "TITLE", "DIRECTOR", "RELEASEDATE", "CAST", "GENRE", "SYNOPSIS", "CREATOR") VALUES (${MovieId.random}, ${input.title}, ${input.director}, ${input.releaseDate}, ${input.cast}, ${input.genre}, ${input.synopsis}, $userId)
      RETURNING *
    """.stripMargin.as[Movie].head.asTry.flatMap {
      case Success(a)                                                     => DBIO.successful(a.asRight[MovieDataError])
      case Failure(e: PSQLException) if e.getSQLState.startsWith("23505") => DBIO.successful(UniqueMovieViolation.asLeft[Movie])
      case Failure(t)                                                     => DBIO.failed(t)
    }
  }

  def find(title: String): DBIO[Option[Movie]] = {
    sql"""
      SELECT "ID", "TITLE", "DIRECTOR", "RELEASEDATE", "CAST", "GENRE", "SYNOPSIS", "CREATOR"
      FROM "MOVIE"
      WHERE "TITLE" = $title
    """.stripMargin.as[Movie].headOption
  }

  def list(filters: MovieFilters): DBIO[Vector[Movie]] = {
    sql"""
      SELECT "ID", "TITLE", "DIRECTOR", "RELEASEDATE", "CAST", "GENRE", "SYNOPSIS", "CREATOR"
      FROM "MOVIE"
      WHERE "TITLE" = ${filters.title}
      OR "RELEASEDATE" = ${filters.date}
      OR "GENRE" = ${filters.genre}
    """.stripMargin.as[Movie]
  }

  def update(userId: UserId, input: MovieInput): DBIO[Option[Movie]] = {
    sql"""
      UPDATE "MOVIE" SET "DIRECTOR" = ${input.director}, "RELEASEDATE" = ${input.releaseDate}, "CAST" = ${input.cast}, "GENRE" = ${input.genre}, "SYNOPSIS" = ${input.synopsis}
      WHERE "MOVIE"."CREATOR" = $userId
      AND "MOVIE"."TITLE" = ${input.title}
      RETURNING "ID", "TITLE", "DIRECTOR", "RELEASEDATE", "CAST", "GENRE", "SYNOPSIS"
    """.stripMargin.as[Movie].headOption
  }

  def delete(userId: UserId, title: String): DBIO[Option[Movie]] = {
    sql"""
      DELETE FROM "MOVIE"
      WHERE "MOVIE"."CREATOR" = $userId AND "MOVIE"."TITLE" = $title
      RETURNING "ID", "TITLE", "DIRECTOR", "RELEASEDATE", "CAST", "GENRE", "SYNOPSIS"
    """.stripMargin.as[Movie].headOption
  }
}
