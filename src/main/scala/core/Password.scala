package core
import com.github.t3hnar.bcrypt._

final case class Password private (value: String) {
  override def toString: String = value
}

object Password {
  val passwordRegexp = """([a-zA-Z0-9]).{2,15}$""".r

  def fromString(v: String): Option[Password] =
    passwordRegexp.findFirstIn(v).filter(_ == v)
      .map(_ => Password(v.bcryptBounded(3)))

  def apply(raw: String): Password = {
    require(raw != null)
    new Password(raw)
  }
}
