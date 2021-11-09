package presentation.Authentication

import io.circe.{Decoder, HCursor}

final case class UserToken(sub: String, exp: Int, iat: Int)

object UserToken {
  implicit val decodeKeycloakToken: Decoder[UserToken] = new Decoder[UserToken] {
    final def apply(c: HCursor): Decoder.Result[UserToken] =
      for {
        sub <- c.downField("sub").as[String]
        exp <- c.downField("exp").as[Int]
        iat <- c.downField("iat").as[Int]
      } yield {
        new UserToken(sub, exp, iat)
      }
  }
}