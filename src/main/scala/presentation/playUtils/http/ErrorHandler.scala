package presentation.playUtils.http

import javax.inject._
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.{BAD_REQUEST, FORBIDDEN, NOT_FOUND}
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router
import play.api.{OptionalSourceMapper, _}
import presentation.playUtils.Error

import scala.concurrent._

class ErrorHandler(
                    env:    Environment,
                    config: Configuration,
                    router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, new OptionalSourceMapper(None), router) {
  /**
   * Invoked in prod mode when a server error occurs.
   * Override this rather than [[onServerError]] if you don't want to change Play's debug output when logging errors
   * in dev mode.
   *
   * @param request
   * @param exception
   * @return
   */
  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {

    Future.successful(InternalServerError(Error(s"A server error occurred logged with id : ${exception.description} ${exception.printStackTrace()} ")))

  }

  /**
   * Invoked when a client error occurs, that is, an error in the 4xx series.
   *
   * @param request    The request that caused the client error.
   * @param statusCode The error status code.  Must be greater or equal to 400, and less than 500.
   * @param message    The error message.
   */
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = statusCode match {
    case BAD_REQUEST =>
      Future.successful(BadRequest(Error(message)))

    case FORBIDDEN =>
      Future.successful(Forbidden(Error(message)))

    case NOT_FOUND =>
      Future.successful(NotFound(
        Error(s"action not found: ${request.method} ${request.path}")))

    case _ =>
      Future.successful(Status(statusCode)("A client error occurred: " + message))
  }
}
