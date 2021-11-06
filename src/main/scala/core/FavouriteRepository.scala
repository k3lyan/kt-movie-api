package core

sealed trait AddToFavouriteDataError
case object UniqueFavouriteViolation extends AddToFavouriteDataError

trait FavouriteRepository[DB[_]] {

  // ADD to favourite
  def create(input: FavouriteInput): DB[Either[AddToFavouriteDataError, Favourite]]

 // REMOVE from favourite
  def delete(favouriteId: FavouriteId): DB[Option[Favourite]]
}