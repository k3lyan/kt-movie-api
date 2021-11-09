package presentation

import core.{FavouriteId, MovieId, UserId}

import java.nio.file.Path
import io.circe.Encoder
import io.circe.Encoder._


trait MovieRegistryEncoder {

  implicit val encodeCheckId: Encoder[MovieId] =
    encodeString.contramap[MovieId](_.toString)

  implicit val encodeDefectId: Encoder[UserId] =
    encodeString.contramap[UserId](_.toString)

  implicit val encodePath: Encoder[Path] =
    encodeString.contramap[Path](_.toString)

  implicit val encodeResourceId: Encoder[FavouriteId] =
    encodeString.contramap[FavouriteId](_.toString)
}
