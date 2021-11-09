package core

import cats.Functor
import cats.data.EitherT

sealed trait MovieServiceError
case object MovieAlreadyExists extends MovieServiceError

final class MovieService[F[_] : Functor, DB[_]](protected val movieRepository: MovieRepository[DB])(implicit val dbManager: Database[F, DB]) {

  def create(userId: UserId, input: MovieInput): F[Either[MovieServiceError, Movie]] =
    EitherT(Database[F, DB].execute(movieRepository.create(userId, input)))
      .leftMap[MovieServiceError] {
        case UniqueMovieViolation => MovieAlreadyExists
      }.value

  def list(filters: MovieFilters): F[Vector[Movie]] =
    Database[F, DB].execute {
      movieRepository.list(filters)
    }

  def find(title: String): F[Option[Movie]] =
    Database[F, DB].execute {
      movieRepository.find(title)
    }

  def update(userId: UserId, input: MovieInput): F[Option[Movie]] =
    Database[F, DB].execute(movieRepository.update(userId, input))

  def delete(userId: UserId, title: String): F[Option[Movie]] =
    Database[F, DB].execute {
      movieRepository.delete(userId, title)
    }
}