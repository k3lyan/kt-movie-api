package core

final case class Pseudo private (value: String) {
  override def toString: String = value
}

object Pseudo {
    val pseudoRegexp = """"^[a-zA-Z0-9]*$""".r

  def fromString(v: String): Option[Pseudo] =
    pseudoRegexp.findFirstIn(v).filter(_ == v)
      .map(_ =>  Pseudo(v))

  def apply(raw: String): Pseudo = {
    require(raw != null)
    new Pseudo(raw)
  }
}


