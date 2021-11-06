package core

import java.util.UUID

final class UserId private (val underlying: UUID) extends AnyVal {
  override def toString: String = underlying.toString
}

object UserId {
  def random: UserId = {
    new UserId(UUID.randomUUID())
  }
  def apply(raw: UUID): UserId = {
    new UserId(raw)
  }
  def apply(str: String): UserId = {
    new UserId(UUID.fromString(str))
  }
}

final case class User(id: UserId, pseudo: String, password: String, email: String)