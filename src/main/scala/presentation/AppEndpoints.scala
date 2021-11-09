package presentation

import buildinfo.BuildInfo
import cats.Monad
import cats.implicits._
import io.circe.Json
import sttp.tapir.Codec.string
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

trait AppEndpoints[F[_]] {

  implicit def F: Monad[F]

  lazy val healthCheck: ServerEndpoint[Unit, Unit, String, Nothing, F] =
    endpoint
      .get
      .summary("")
      .tag("Admin")
      .in("admin" / "healthcheck")
      .out(plainBody[String])
      .serverLogic { _ =>
        "OK".asRight[Unit].pure[F]
      }

  lazy val buildinfo: ServerEndpoint[Unit, Unit, Json, Nothing, F] =
    endpoint
      .get
      .summary("")
      .tag("Admin")
      .in("admin" / "buildinfo")
      .out(jsonBody[Json])
      .serverLogic { _ =>
        Json.fromFields(List(
          "name" -> Json.fromString(BuildInfo.name),
          "version" -> Json.fromString(BuildInfo.version),
          "scalaVersion" -> Json.fromString(BuildInfo.scalaVersion),
          "sbtVersion" -> Json.fromString(BuildInfo.sbtVersion),
          "gitBranch" -> Json.fromString(BuildInfo.gitBranch),
          "buildDate" -> Json.fromString(BuildInfo.buildDate))).asRight[Unit].pure[F]
      }

}