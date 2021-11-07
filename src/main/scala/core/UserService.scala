package core

import cats.Functor
import cats.data.EitherT

sealed trait UserServiceError
case object UserAlreadyExists extends UserServiceError

final class UserService[F[_] : Functor, DB[_]](protected val userRepository: UserRepository[DB])(implicit val dbManager: Database[F, DB]) {

  def create(input: UserInput): F[Either[UserServiceError, User]] =
    EitherT(Database[F, DB].execute(userRepository.create(input)))
      .leftMap[UserServiceError] {
        case UniqueUserViolation => UserAlreadyExists
      }.value

  def find(input: UserInput): F[Option[User]] =
    Database[F, DB].execute {
      userRepository.find(input)
    }

//  def delete(input: UserInput): F[Option[User]] =
//    Database[F, DB].execute {
//      userRepository.delete(input)
//    }
}