package presentation

import akka.stream.Materializer
import cats.Monad
import core.{FavouriteService, MovieService, UserService}
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import slick.dbio.DBIO
import sttp.tapir.server.play._
import sttp.tapir.server.play.RichPlayServerEndpoint
import sttp.tapir.swagger.play.SwaggerPlay

import scala.concurrent.{ExecutionContext, Future}

class PlayQualityRouter(protected val userService: UserService[Future, DBIO],
                        protected val movieService: MovieService[Future, DBIO],
                        protected val favouriteService: FavouriteService[Future, DBIO])
                       (implicit val mat: Materializer, ec: ExecutionContext)
  extends MovieRegistryEndpoints[Future, DBIO] with SimpleRouter {

  override val F: Monad[Future] = implicitly[Monad[Future]]

  override def routes: Routes = {
    new SwaggerPlay(openApiYml).routes
      .orElse(healthCheck.toRoute)
      .orElse(buildinfo.toRoute)
      .orElse(register.toRoute)
      .orElse(login.toRoute)
      .orElse(logout.toRoute)
      .orElse(createMovie.toRoute)
      .orElse(getMovieDetails.toRoute)
      .orElse(listMovies.toRoute)
      .orElse(updateMovie.toRoute)
      .orElse(deleteMovie.toRoute)
      .orElse(addFavourite.toRoute)
      .orElse(deleteFavourite.toRoute)
  }
}