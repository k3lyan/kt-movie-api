package core

final class ReleaseDate private (val underlying: String) extends AnyVal {
  override def toString: String = underlying.toString
}

object ReleaseDate {
  val dateRegex = """([0-9]{4}-[0-9]{2}-[0-9]{2})"""
  val Date = dateRegex.r


  def apply(raw: Option[String]): Option[ReleaseDate]= raw match {
    case Some(date) =>
      date match {
        case Date(d) => Some(new ReleaseDate(d))
        case _ => throw TimeCodeException("Badly formatted Release Date.")
      }
    case None => None
  }
}

case class TimeCodeException(message: String) extends Exception(message)
