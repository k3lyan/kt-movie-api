package core

import java.time.LocalDate

final class ReleaseDate private (val underlying: String) extends AnyVal {
  def toLocalDate: LocalDate = LocalDate.parse(underlying)
}

object ReleaseDate {
  val dateRegex = """([0-9]{4}-[0-9]{2}-[0-9]{2})"""
  val Date = dateRegex.r


  def apply(raw: String): Option[ReleaseDate]= raw match {
        case Date(d) => Some(new ReleaseDate(d))
        case _ => throw TimeCodeException("Badly formatted Release Date.")
      }

}

case class TimeCodeException(message: String) extends Exception(message)
