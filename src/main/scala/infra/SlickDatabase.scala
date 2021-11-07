package infra

import core.Database
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile
import infra.MovieRegistryPostgresProfile.api._

import scala.concurrent.Future

class SlickDatabase(db: JdbcProfile#Backend#Database) extends Database[Future, DBIO] {

  override def execute[A](action: DBIO[A]): Future[A] = db.run(action)
  override def executeTransactionally[A](action: DBIO[A]): Future[A] = db.run(action.transactionally)
}