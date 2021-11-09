package core

sealed trait UserDataError
case object UniqueUserViolation extends UserDataError



trait UserRepository [DB[_]] {
  // REGISTER user
  def create(pseudo: String, password: String): DB[Either[UserDataError, UserId]]

  // GET for login check
  def find(pseudo: String): DB[Option[User]]

  // GET for logout check
  def get(id: UserId): DB[Option[User]]
}

