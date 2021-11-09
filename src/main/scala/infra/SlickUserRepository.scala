package infra

import cats.implicits.catsSyntaxEitherId
import core.{UniqueUserViolation, User, UserDataError, UserId, UserInput, UserRepository}
import org.postgresql.util.PSQLException
import slick.dbio.DBIO
import infra.MovieRegistryPostgresProfile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class SlickUserRepository (implicit val ec: ExecutionContext) extends UserRepository[DBIO] {

  // REGISTER
  def create(pseudo: String, password: String): DBIO[Either[UserDataError, UserId]] = {
    sql"""
      INSERT INTO "USER" ("ID", "PSEUDO", "PASSWORD") VALUES (${UserId.random}, $pseudo, $password)
      RETURNING "ID"
    """.stripMargin.as[UserId].head.asTry.flatMap {
      case Success(a)                                                     => DBIO.successful(a.asRight[UserDataError])
      case Failure(e: PSQLException) if e.getSQLState.startsWith("23505") => DBIO.successful(UniqueUserViolation.asLeft[UserId])
      case Failure(t)                                                     => DBIO.failed(t)
    }
  }

  // GET (login)
  def find(pseudo: String): DBIO[Option[User]] = {
    sql"""
          SELECT "ID", "PSEUDO", "PASSWORD"
          FROM "USER"
          WHERE "USER"."PSEUDO" = $pseudo
    """.stripMargin.as[User].headOption
  }

  // GET (logout)
  def get(id: UserId): DBIO[Option[User]] = {
    sql"""
     SELECT "ID", "PSEUDO", "PASSWORD"
     FROM "USER"
     WHERE "USER"."ID" = $id
    """.stripMargin.as[User].headOption
  }
}