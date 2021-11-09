package presentation

import cats.data.{EitherT, OptionT}
import cats.implicits._
import core.{Favourite, FavouriteAlreadyExists, FavouriteInput, FavouriteService, Movie, MovieAlreadyExists, MovieFilters, MovieId, MovieInput, MovieService, Password, Pseudo, User, UserAlreadyExists, UserId, UserIdentificationError, UserInput, UserService}
import io.circe.generic.auto._
import io.circe.parser._
import pdi.jwt.{Jwt, JwtOptions}
import presentation.Authentication.LoginResponse.{decodeUser, encodeUser, logoutUser}
import presentation.Authentication.{LoginResponse, UserToken}
import sttp.model.StatusCode
import sttp.tapir.Codec.string
import sttp.tapir.docs.openapi._
import com.github.t3hnar.bcrypt._

import java.time.LocalDate
import scala.util.Try
import sttp.tapir.json.circe._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.{PartialServerEndpoint, ServerEndpoint}
import sttp.tapir.{endpoint, path, statusCode, _}

import scala.language.existentials

case class Error(msg: String, statusCode: StatusCode)

trait MovieRegistryEndpoints[F[_], DB[_]] extends AppEndpoints[F]
  with MovieRegistryDecoder
  with MovieRegistryEncoder
  with TapirCodec {

  protected val userService: UserService[F, DB]
  protected val movieService: MovieService[F, DB]
  protected val favouriteService: FavouriteService[F, DB]

  type MovieRegistryServerEndpoint[I, O] = ServerEndpoint[(T, I), Error, O, Nothing, F] forSome {type T}

  private lazy val securedEndpoint: PartialServerEndpoint[UserToken, Unit, Error, Unit, Nothing, F] =
    endpoint
      .in(auth.bearer[String])
      .errorOut(stringBody.and(statusCode).mapTo(Error))
      .serverLogicForCurrent[UserToken, F](decodeUser(_).pure[F])


  private def checkUser(psd: String, pwd: String): F[Either[Error, UserInput]] =
    EitherT.fromEither[F](
      Try {
      (Pseudo.fromString(psd), Password.fromString(pwd)) match {
        case (Some(pseudo), Some(password)) => {
          Right(UserInput(pseudo.toString, password.toString))
        }
        case _ => throw new IllegalArgumentException("Wrong username or password format.")
      }
    }.getOrElse(Left(UserIdentificationError))
    ).leftMap[Error]{
      case UserIdentificationError =>  Error(s"Pseudo or Password with a wrong format", StatusCode.Unauthorized)
    }.value


  lazy val register: ServerEndpoint[UserInput, Error, UserId, Nothing, F] =
    endpoint
      .post
      .tag("User")
      .in("register")
      .in(jsonBody[UserInput])
      .out(jsonBody[UserId])
      .out(statusCode(StatusCode.Created))
      .errorOut(stringBody.and(statusCode).mapTo(Error))
      .serverLogic {
        input: UserInput =>
          EitherT(userService.create(input.pseudo, input.password.boundedBcrypt))
              .leftMap[Error] {
                case UserAlreadyExists => Error(s"User already exists with this pseudo", StatusCode.Conflict)
                case UserIdentificationError => Error(s"Pseudo or Password with a wrong format", StatusCode.Unauthorized)
                case _ => Error("We encountered an error Sir!", StatusCode.InternalServerError)
              }.value
      }

    lazy val login: ServerEndpoint[UserInput, Error, LoginResponse, Nothing, F] =
      endpoint
        .post
        .tag("User")
        .in("login")
        .in(jsonBody[UserInput])
        .out(jsonBody[LoginResponse])
        .out(statusCode(StatusCode.Created))
        .errorOut(stringBody.and(statusCode).mapTo(Error))
        .serverLogic {
          input: UserInput =>
            OptionT(userService
              .find(input.pseudo))
              .map((user: User) => encodeUser(user.id, input.password, user.password).asRight[Error])
              .getOrElse(Error("User does not exist, impossible to login.", StatusCode.NotFound).asLeft[LoginResponse])
        }

  lazy val logout: MovieRegistryServerEndpoint[Unit, LoginResponse] =
    securedEndpoint
      .post
      .tag("User")
      .in("logout")
      .out(jsonBody[LoginResponse])
      .out(statusCode(StatusCode.Created))
      .serverLogic {
        case (token: UserToken, _) =>
          OptionT(userService.get({
            println(s"SUB: ${token.sub}")
            UserId(token.sub)
          }))
            .map((user: User) => logoutUser(user.id).asRight[Error])
            .getOrElse(Error("User does not exist", StatusCode.NotFound).asLeft[LoginResponse])
      }

            lazy val createMovie: MovieRegistryServerEndpoint[MovieInput, Movie] =
              securedEndpoint
                .post
                .tag("Movie")
                .in("movies")
                .in(jsonBody[MovieInput])
                .out(jsonBody[Movie])
                .out(statusCode(StatusCode.Created))
                .serverLogic {
                  case (token: UserToken, input: MovieInput) =>
                    EitherT(movieService.create(UserId(token.sub), input))
                      .leftMap[Error] {
                        case MovieAlreadyExists => Error(s"Movie already exist with this title", StatusCode.Conflict)
                      }.value
                }


            lazy val getMovieDetails: MovieRegistryServerEndpoint[String, Movie] =
              securedEndpoint
                .get
                .tag("Movie")
                .in("movies" / path[String]("title"))
                .out(jsonBody[Movie])
                .serverLogic {
                  case (_: UserToken, title: String) =>
                    OptionT(movieService.find(title))
                      .map(_.asRight[Error])
                      .getOrElse(Error("No movie found with this title", StatusCode.NotFound).asLeft[Movie])
                }

            lazy val listMovies: MovieRegistryServerEndpoint[(String, Option[LocalDate], Option[String]), Vector[Movie]] =
              securedEndpoint
                .get
                .tag("Movie")
                .in("movies" / query[String]("title") / query[Option[LocalDate]]("releaseDate") / query[Option[String]]("genre"))
                .out(jsonBody[Vector[Movie]])
                .serverLogic {
                  case (_: UserToken, filters: (String, Option[LocalDate], Option[String])) =>
                    movieService.list(MovieFilters(filters._1, filters._2, filters._3))
                      .map(_.asRight[Error])
                }

            lazy val updateMovie: MovieRegistryServerEndpoint[(String, MovieInput), Movie] =
              securedEndpoint
                .put
                .tag("Movie")
                .in("movies" / path[String]("title"))
                .in(jsonBody[MovieInput])
                .out(jsonBody[Movie])
                .serverLogic {
                  case (token: UserToken, (_, input)) =>
                    OptionT(movieService.update(UserId(token.sub), input))
                      .map(_.asRight[Error])
                      .getOrElse(Error("Movie cannot be updated", StatusCode.Unauthorized).asLeft[Movie])
                }


            lazy val deleteMovie: MovieRegistryServerEndpoint[String, Movie] =
              securedEndpoint
                .delete
                .tag("Movie")
                .in("movies" / path[String]("title"))
                .out(jsonBody[Movie])
                .serverLogic {
                  case (token: UserToken, title: String) =>
                    OptionT(movieService.delete(UserId(token.sub), title))
                      .map(_.asRight[Error])
                      .getOrElse(Error("Defect does not exist", StatusCode.NotFound).asLeft[Movie])
                }


            lazy val addFavourite: MovieRegistryServerEndpoint[FavouriteInput, Favourite] =
              securedEndpoint
                .post
                .tag("Favourite")
                .in("favourites")
                .in(jsonBody[FavouriteInput])
                .out(jsonBody[Favourite])
                .out(statusCode(StatusCode.Created))
                .serverLogic {
                  case (token: UserToken, input: FavouriteInput) =>
                    EitherT(favouriteService.create(input.title, UserId(token.sub)))
                      .leftMap[Error] {
                        case FavouriteAlreadyExists => Error(s"Favourite already exists for this user", StatusCode.Conflict)
                      }.value
                }


            lazy val deleteFavourite: MovieRegistryServerEndpoint[String, Favourite] =
              securedEndpoint
                .delete
                .tag("Favourite")
                .in("favourites" / path[String]("title"))
                .out(jsonBody[Favourite])
                .serverLogic {
                  case (token: UserToken, title: String) =>
                    OptionT(favouriteService.delete(title, UserId(token.sub)))
                      .map(_.asRight[Error])
                      .getOrElse(Error("Favourite does not exist", StatusCode.NotFound).asLeft[Favourite])
                }


            lazy val openApiYml: String = List(
              register.endpoint,
              login.endpoint,
              logout.endpoint,
              createMovie.endpoint,
              getMovieDetails.endpoint,
              listMovies.endpoint,
              updateMovie.endpoint,
              deleteMovie.endpoint,
              addFavourite.endpoint,
              deleteFavourite.endpoint,
            )
              .toOpenAPI("MovieRegistry Service", "1.1")
              .toYaml
}
