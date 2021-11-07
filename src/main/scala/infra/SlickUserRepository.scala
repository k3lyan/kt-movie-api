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
  def create(input: UserInput): DBIO[Either[UserDataError, User]] = {
    sql"""
      INSERT INTO "USER" ("ID", "PSEUDO", "PASSWORD") VALUES (${UserId.random}, ${input.pseudo.toString}, ${input.password.toString})
      RETURNING "ID", "TITLE", "USERID"
    """.stripMargin.as[User].head.asTry.flatMap {
      case Success(a)                                                     => DBIO.successful(a.asRight[UserDataError])
      case Failure(e: PSQLException) if e.getSQLState.startsWith("23505") => DBIO.successful(UniqueUserViolation.asLeft[User])
      case Failure(t)                                                     => DBIO.failed(t)
    }
  }

  // GET (login)
  def find(input: UserInput): DBIO[Option[User]] = {
    sql"""
     SELECT "ID", "PSEUDO", "PASSWORD"
      WHERE "USER"."PSEUDO" = ${input.pseudo} AND "USER"."PASSWORD" = ${input.password}
      RETURNING "ID", "PSEUDO", "PASSWORD"
    """.stripMargin.as[User].headOption
  }
}