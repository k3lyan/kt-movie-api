package core

import com.github.t3hnar.bcrypt._
final case class Pseudo private (value: String) {
  override def toString: String = value
}

object Pseudo {
    val pseudoRegexp = """^[a-zA-Z0-9]*$""".r

  def fromString(v: String): Option[Pseudo] =
    pseudoRegexp.findFirstIn(v).filter(_ == v)
      .map(_ =>  Pseudo(v))

  def apply(raw: String): Pseudo = {
    require(raw != null)
    new Pseudo(raw)
  }
  println("string".boundedBcrypt)
  println("string".isBcryptedBounded("$2a$10$WRRpC4uiZIRCS7Uz2u/RSedm1lHJ6Duidhaa8I//Vk88au0zXRr1q"))
}


