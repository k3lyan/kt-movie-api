package core

import cats.Functor
import cats.data.EitherT

sealed trait UserServiceError
case object UserAlreadyExists extends UserServiceError
case object UserIdentificationError extends UserServiceError


final class UserService[F[_] : Functor, DB[_]](protected val userRepository: UserRepository[DB])(implicit val dbManager: Database[F, DB]) {

  def create(pseudo: String, password: String): F[Either[UserServiceError, UserId]] = {
    EitherT(Database[F, DB].execute(userRepository.create(pseudo, password)))
      .leftMap[UserServiceError] {
        case UniqueUserViolation => UserAlreadyExists
      }.value
  }

  def find(pseudo: String): F[Option[User]] =
    Database[F, DB].execute {
      userRepository.find(pseudo)
    }

  def get(id: UserId): F[Option[User]] =
    Database[F, DB].execute {
      userRepository.get(id)
    }
}