package core

import java.util.UUID

final class FavouriteId private (val underlying: UUID) extends AnyVal {
  override def toString: String = underlying.toString
}

object FavouriteId {
  def random: FavouriteId = {
    new FavouriteId(UUID.randomUUID())
  }
  def apply(raw: UUID): FavouriteId = {
    new FavouriteId(raw)
  }
  def apply(str: String): FavouriteId = {
    new FavouriteId(UUID.fromString(str))
  }
}

final case class Favourite(id: FavouriteId, title: String, userId: UserId)
