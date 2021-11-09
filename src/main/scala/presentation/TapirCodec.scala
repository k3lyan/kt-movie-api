package presentation

import core.{FavouriteId, MovieId, ReleaseDate, UserId}
import sttp.tapir.Codec
import sttp.tapir.CodecFormat.TextPlain

trait TapirCodec {

  implicit val movieIdCodec: Codec[String, MovieId, TextPlain] =
    sttp.tapir.Codec.string.map(MovieId(_))(_.toString)

  implicit val userIdCodec: Codec[String, UserId, TextPlain] =
    sttp.tapir.Codec.string.map(UserId(_))(_.toString)

  implicit val favouriteIdCodec: Codec[String, FavouriteId, TextPlain] =
    sttp.tapir.Codec.string.map(FavouriteId(_))(_.toString)

  implicit val releaseDateCodec: Codec[String, Option[ReleaseDate], TextPlain] =
    sttp.tapir.Codec.string.map(ReleaseDate(_))(_.toString)
}