package presentation

import cats.implicits.catsSyntaxEitherId
import core.{FavouriteId, MovieId, UserId}
import io.circe.Decoder
import io.circe.Decoder.decodeString

import java.nio.file.{Path, Paths}
import scala.util.Try

trait MovieRegistryDecoder {

  implicit val decodeMovieId: Decoder[MovieId] =
    decodeString.emap(str => MovieId(str).asRight)

  implicit val decodeUserId: Decoder[UserId] =
    decodeString.emap(str => UserId(str).asRight)

  implicit val decodeFavouriteId: Decoder[FavouriteId] =
    decodeString.emap(str => FavouriteId(str).asRight)

  implicit val decodePath: Decoder[Path] =
    decodeString.emapTry(str => Try(Paths.get(str)))
}
