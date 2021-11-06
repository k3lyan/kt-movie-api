package core

import cats.Functor
import cats.data.EitherT


sealed trait FavouriteServiceError
case object FavouriteAlreadyExists extends FavouriteServiceError

final class FavouriteService[F[_] : Functor, DB[_]](protected val favouriteRepository: FavouriteRepository[DB])(implicit val dbManager: Database[F, DB]) {

  def create(input: FavouriteInput): F[Either[FavouriteServiceError, Favourite]] =
    EitherT(Database[F, DB].execute(favouriteRepository.create(input)))
      .leftMap[FavouriteServiceError] {
        case UniqueFavouriteViolation => FavouriteAlreadyExists
      }.value


  def delete(id: FavouriteId): F[Option[Favourite]] =
    Database[F, DB].execute {
      favouriteRepository.delete(id)
    }
}
