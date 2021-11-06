package core

import cats.Functor
import cats.data.EitherT

sealed trait MovieServiceError
case object MovieAlreadyExists extends MovieServiceError

final class MovieService[F[_] : Functor, DB[_]](protected val movieRepository: MovieRepository[DB])(implicit val dbManager: Database[F, DB]) {

  def create(input: MovieInput): F[Either[MovieServiceError, Movie]] =
    EitherT(Database[F, DB].execute(movieRepository.create(input)))
      .leftMap[MovieServiceError] {
        case UniqueMovieViolation => MovieAlreadyExists
      }.value

  def list(title: String, date: Option[ReleaseDate], genre: Option[String]): F[Vector[Movie]] =
    Database[F, DB].execute {
      movieRepository.list(title, date, genre)
    }

  def find(title: String): F[Option[Movie]] =
    Database[F, DB].execute {
      movieRepository.find(title)
    }

  //TODD: Change id by title? As title will be a Unique key.
  def update(id: MovieId, input: MovieInput): F[Either[MovieServiceError, Option[Movie]]] =
    EitherT(Database[F, DB].execute(movieRepository.update(id, input)))
      .leftMap[MovieServiceError] {
        case UniqueMovieViolation => MovieAlreadyExists
      }.value

  //TODD: Change id by title? As title will be a Unique key.
  def delete(id: MovieId, creator: UserId): F[Option[Movie]] =
    Database[F, DB].execute {
      movieRepository.delete(id, creator)
    }
}