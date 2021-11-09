package presentation.Authentication

import cats.implicits.catsSyntaxOptionId
import core.UserId

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import com.github.t3hnar.bcrypt._
import presentation.Error
import sttp.model.StatusCode

final case class LoginResponse(token: String, exp: Int)

object LoginResponse {
  // TODO: Add in conf
  private val key = "secretKey"
  private val algo = JwtAlgorithm.HS256

  def encodeUser(userId: UserId, password: String, hash: String): LoginResponse = {
    val exp = 157784760
    def claim(id: String) = JwtClaim(
      subject = id.some,
      expiration = Some(Instant.now.plusSeconds(exp).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    if (password.isBcryptedBounded(hash)) {
      LoginResponse(JwtCirce.encode(claim(userId.toString), key, algo), exp)
    }
    else LoginResponse("wrong", exp)
  }

  def logoutUser(userId: UserId): LoginResponse = {
    val exp = 0
    def claim(psd: String) = JwtClaim(
      subject = psd.some,
      expiration = Some(Instant.now.plusSeconds(exp).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    LoginResponse(JwtCirce.encode(claim(userId.toString), key, algo), exp)
  }

  def decodeUser(token: String): Either[Error, UserToken] = {
    JwtCirce
      .decodeJson(token, key, Seq(algo))
      .toEither
      .flatMap(_.as[UserToken])
      .left.map[Error](_ => Error("Invalid User Token.", StatusCode.Unauthorized))
  }

}
