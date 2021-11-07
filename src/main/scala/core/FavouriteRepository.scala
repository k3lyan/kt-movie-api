package core

sealed trait AddToFavouriteDataError
case object UniqueFavouriteViolation extends AddToFavouriteDataError

trait FavouriteRepository[DB[_]] {

  // ADD to favourite
  def create(title: String, userId: UserId): DB[Either[AddToFavouriteDataError, Favourite]]

 // REMOVE from favourite
  def delete(title: String, userId: UserId): DB[Option[Favourite]]
}