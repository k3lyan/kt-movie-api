package core

import java.util.UUID

final class MovieId private (val underlying: UUID) extends AnyVal {
  override def toString: String = underlying.toString
}

object MovieId {
  def random: MovieId = {
    new MovieId(UUID.randomUUID())
  }
  def apply(raw: UUID): MovieId = {
    new MovieId(raw)
  }
  def apply(str: String): MovieId = {
    new MovieId(UUID.fromString(str))
  }
}

case class Movie(
                  id:            MovieId,
                  title:         String,
                  director:      String,
                  releaseDate:   Option[ReleaseDate],
                  cast:          Option[String],
                  genre:         Option[String],
                  synopsis:      Option[String],
                  creator:       UserId)

