package infra

import cats.implicits.catsSyntaxEitherId
import core.{AddToFavouriteDataError, Favourite, FavouriteId, FavouriteRepository, UniqueFavouriteViolation, UserId}
import org.postgresql.util.PSQLException
import slick.dbio.DBIO
import MovieRegistryPostgresProfile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class SlickFavouriteRepository(implicit val ec: ExecutionContext) extends FavouriteRepository[DBIO] {

  // ADD
  def create(title: String, userId: UserId): DBIO[Either[AddToFavouriteDataError, Favourite]] = {
    sql"""
      INSERT INTO "FAVOURITE" ("ID", "TITLE", "USERID") VALUES (${FavouriteId.random}, ${title}, ${userId})
      RETURNING "ID", "TITLE", "USERID"
    """.stripMargin.as[Favourite].head.asTry.flatMap {
      case Success(a)                                                     => DBIO.successful(a.asRight[AddToFavouriteDataError])
      case Failure(e: PSQLException) if e.getSQLState.startsWith("23505") => DBIO.successful(UniqueFavouriteViolation.asLeft[Favourite])
      case Failure(t)                                                     => DBIO.failed(t)
    }
  }

  // REMOVE
  def delete(title: String, userId: UserId): DBIO[Option[Favourite]] = {
    sql"""
      DELETE FROM "FAVOURITE"
      WHERE "FAVOURITE"."TITLE" = ${title} AND "FAVOURITE"."USERID" = $userId
      RETURNING "ID", "TITLE", "USERID"
    """.stripMargin.as[Favourite].headOption
  }
}
