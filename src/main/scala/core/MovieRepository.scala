package core

sealed trait CreateOrUpdateMovieDataError
case object UniqueMovieViolation extends CreateOrUpdateMovieDataError

trait MovieRepository[DB[_]] {

  // CREATE film
  def create(input: MovieInput): DB[Either[CreateOrUpdateMovieDataError, Movie]]

  // GET films list with filters
  def list(title: String, date: Option[ReleaseDate], genre: Option[String]): DB[Vector[Movie]]

  // GET film details
  def find(title: String): DB[Option[Movie]]

  // UPDATE a film
  def update(id: MovieId, input: MovieInput): DB[Either[CreateOrUpdateMovieDataError,Option[Movie]]]

  // DELETE a film
  def delete(id: MovieId, creator: UserId): DB[Option[Movie]]
}
