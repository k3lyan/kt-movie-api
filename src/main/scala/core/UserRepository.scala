package core

sealed trait UserDataError
case object UniqueUserViolation extends UserDataError

trait UserRepository [DB[_]] {

  // REGISTER user
  def create(input: UserInput): DB[Either[UserDataError, User]]

  // GET for login check
  def find(input: UserInput): DB[Option[User]]

  // DELETE a user (no implementation yet)
  //def delete(input: UserInput): DB[Option[User]]
}

