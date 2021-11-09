import core.{Database, FavouriteRepository, FavouriteService, MovieRepository, MovieService, UserRepository, UserService}
import infra.{SlickDatabase, SlickFavouriteRepository, SlickMovieRepository, SlickUserRepository}
import org.slf4j.LoggerFactory
import play.api.db.slick.DbName
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.core.server.ServerComponents
import presentation.playUtils.filters.LoggingFilter
import presentation.playUtils.http.ErrorHandler
import presentation.{MovieRegistryServerComponents, PlayQualityRouter}
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object Main extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)
  println("Start application")

  val components: ServerComponents = new MovieRegistryServerComponents {

    implicit val databaseManager: Database[Future, DBIO] =
      new SlickDatabase(slickApi.dbConfig[JdbcProfile](DbName("movieRegistry")).db)

    private lazy val userRepository: UserRepository[DBIO] =
      new SlickUserRepository()

    private lazy val userService: UserService[Future, DBIO] =
      new UserService[Future, DBIO](userRepository)

    private lazy val movieRepository: MovieRepository[DBIO] =
      new SlickMovieRepository()

    private lazy val movieService: MovieService[Future, DBIO] =
      new MovieService[Future, DBIO](movieRepository)

    private lazy val favouriteRepository: FavouriteRepository[DBIO] =
      new SlickFavouriteRepository()

    private lazy val favouriteService: FavouriteService[Future, DBIO] =
      new FavouriteService[Future, DBIO](favouriteRepository)


    // BuiltInComponents
    override lazy val router: Router =
      new PlayQualityRouter(userService, movieService, favouriteService)

    lazy val loggingFilter: LoggingFilter = new LoggingFilter()

    override def httpFilters: Seq[EssentialFilter] = Seq(corsFilter, loggingFilter)

    // HttpErrorHandlerComponents
    override lazy val httpErrorHandler =
      new ErrorHandler(environment, configuration, () => router)
  }

  val server = components.server
}
