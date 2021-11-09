package core

sealed trait MovieDataError
case object UniqueMovieViolation extends MovieDataError

trait MovieRepository[DB[_]] {

  // CREATE film
  def create(userId: UserId, input: MovieInput): DB[Either[MovieDataError, Movie]]

  // GET films list with filters
  def list(filters: MovieFilters): DB[Vector[Movie]]

  // GET film details
  def find(title: String): DB[Option[Movie]]

  // UPDATE a film
  def update(userId: UserId, input: MovieInput): DB[Option[Movie]]

  // DELETE a film
  def delete(userId: UserId, title: String): DB[Option[Movie]]
}
