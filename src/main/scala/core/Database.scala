package core

trait Database[F[_], DB[_]] {
  def execute[A](action: DB[A]): F[A]
  def executeTransactionally[A](action: DB[A]): F[A]
}

object Database {
  @inline def apply[F[_], DB[_]](implicit instance: Database[F, DB]): Database[F, DB] = instance
}
