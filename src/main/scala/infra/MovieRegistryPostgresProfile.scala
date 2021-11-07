package infra

import com.github.tminglei.slickpg.{ExPostgresProfile, PgCirceJsonSupport, PgDate2Support}
import core.{Favourite, FavouriteId, Movie, MovieId, Password, Pseudo, ReleaseDate, User, UserId}
import slick.jdbc.{GetResult, PositionedParameters, PositionedResult, SetParameter}

import java.nio.file.Path
import java.util.UUID

trait MovieRegistryPostgresProfile extends ExPostgresProfile
  with PgDate2Support
  with PgCirceJsonSupport {

  override val pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json
  override val api: API = new API {}

  trait API extends super.API
    with Date2DateTimePlainImplicits
    with JsonImplicits
    with CirceJsonPlainImplicits {

    implicit object GetUser extends GetResult[User] {
      def apply(rs: PositionedResult) =
        User(
          id         = UserId(rs.nextObject().asInstanceOf[UUID]),
          pseudo     = Pseudo(rs.nextString),
          password   = Password(rs.nextString))
    }

    implicit object GetMovie extends GetResult[Movie] {
      def apply(rs: PositionedResult): Movie =
        Movie(
          id            = MovieId(rs.nextObject().asInstanceOf[UUID]),
          title         = rs.nextString(),
          director      = rs.nextString(),
          releaseDate   = ReleaseDate(rs.nextStringOption()),
          cast          = rs.nextStringOption(),
          genre         = rs.nextStringOption(),
          synopsis      = rs.nextStringOption())
    }

    implicit object GetFavourite extends GetResult[Favourite] {
      def apply(rs: PositionedResult) =
        Favourite(
          id         = FavouriteId(rs.nextObject().asInstanceOf[UUID]),
          title      = rs.nextString,
          userId    = UserId(rs.nextString))
    }

    implicit object SetUserId extends SetParameter[UserId] {
      def apply(v: UserId, pp: PositionedParameters): Unit =
        pp.setObject(v.underlying, columnTypes.uuidJdbcType.sqlType)
    }

    implicit object SetMovieId extends SetParameter[MovieId] {
      def apply(v: MovieId, pp: PositionedParameters): Unit =
        pp.setObject(v.underlying, columnTypes.uuidJdbcType.sqlType)
    }

    implicit object SetFavouriteId extends SetParameter[FavouriteId] {
      def apply(v: FavouriteId, pp: PositionedParameters): Unit =
        pp.setObject(v.underlying, columnTypes.uuidJdbcType.sqlType)
    }

    implicit object SetPath extends SetParameter[Path] {
      def apply(v: Path, pp: PositionedParameters): Unit =
        pp.setString(v.toString)
    }

    implicit object SetPseudo extends SetParameter[Pseudo] {
      def apply(v: Pseudo, pp: PositionedParameters): Unit =
        pp.setString(v.toString)
    }

    implicit object SetPassword extends SetParameter[Password] {
      def apply(v: Password, pp: PositionedParameters): Unit =
        pp.setString(v.toString)
    }

    implicit val setOptionReleaseDate: SetParameter[Option[ReleaseDate]] = SetParameter[Option[ReleaseDate]] {
      case (Some(v: ReleaseDate), pp: PositionedParameters) => pp.setObject(v.toLocalDate, columnTypes.localDateType.sqlType)
      // OR directly the string?
      //case (Some(v: ReleaseDate), pp: PositionedParameters) => pp.setObject(v.underlying, columnTypes.localDateType.sqlType)
      case (None, pp: PositionedParameters)                 => pp.setNull(columnTypes.localDateType.sqlType)
    }
  }
}

object MovieRegistryPostgresProfile extends MovieRegistryPostgresProfile