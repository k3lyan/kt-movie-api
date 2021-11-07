import sbt._

object Version {

  val cats                   = "2.4.0"
  val chimney                = "0.6.1"
  val circe                  = "0.14.1"
  val jwt                    = "4.3.0"
  val play                   = "2.8.7"
  val playCirce              = "2814.2"
  val playJson               = "2.9.2"
  val playSlick              = "5.0.0"
  val postgresql             = "42.2.5"
  val scalaTestContainers    = "0.39.9"
  val scalatest              = "4.0.3"
  val scala                  = "2.13.4"
  val scalaUri               = "2.3.1"
  val slickPg                = "0.19.7"
  val slickCats              = "0.10.4"
  val tapir                  = "0.18.3"
  val bCryptScala            = "4.3.0"
}

object Library {

  val catsEffect               = "org.typelevel"               %% "cats-effect"                     % Version.cats
  val catsFree                 = "org.typelevel"               %% "cats-free"                       % Version.cats
  val circe                    = "io.circe"                    %% "circe-generic-extras"            % Version.circe
  val chimney                  = "io.scalaland"                %% "chimney"                         % Version.chimney
  val jwtCore                  = "com.pauldijou"               %% "jwt-core"                        % Version.jwt
  val bCryptScala              = "com.github.t3hnar"           %% "scala-bcrypt"                    % Version.bCryptScala
  val playAkkaHttpServer       = "com.typesafe.play"           %% "play-akka-http-server"           % Version.play
  val playFilterHelpers        = "com.typesafe.play"           %% "filters-helpers"                 % Version.play
  val playJson                 = "com.typesafe.play"           %% "play-json"                       % Version.playJson
  val playSlick                = "com.typesafe.play"           %% "play-slick"                      % Version.playSlick
  val postgresql               = "org.postgresql"               % "postgresql"                      % Version.postgresql
  val scalatestPlusPlay        = "org.scalatestplus.play"      %% "scalatestplus-play"              % Version.scalatest
  val scalaUri                 = "io.lemonlabs"                %% "scala-uri"                       % Version.scalaUri
  val slickPg                  = "com.github.tminglei"         %% "slick-pg"                        % Version.slickPg
  val slickPgCirceJson         = "com.github.tminglei"         %% "slick-pg_circe-json"             % Version.slickPg
  val slickPgPlayJson          = "com.github.tminglei"         %% "slick-pg_play-json"              % Version.slickPg
  val slickCats                = "com.rms.miu"                 %% "slick-cats"                      % Version.slickCats
  val tapirEnumeratum          = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"                % Version.tapir
  val tapirJsonCirce           = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"                % Version.tapir
  val tapirOpenApiCirce        = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe"             % Version.tapir
  val tapirOpenApiCirceYaml    = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"        % Version.tapir
  val tapirOpenApiDocs         = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"              % Version.tapir
  val tapirPlayServer          = "com.softwaremill.sttp.tapir" %% "tapir-play-server"               % Version.tapir
  val tapirRedocPlay           = "com.softwaremill.sttp.tapir" %% "tapir-redoc-play"                % Version.tapir
  val tapirSwaggerUiPlay       = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-play"           % Version.tapir
  val testContainersPostgresql = "com.dimafeng"                %% "testcontainers-scala-postgresql" % Version.scalaTestContainers
  val testContainersScalaTest  = "com.dimafeng"                %% "testcontainers-scala-scalatest"  % Version.scalaTestContainers
}

object Dependencies {

  import Library._

  val ktMovieApi: Seq[ModuleID] = Seq(
    catsEffect,
    catsFree,
    circe,
    chimney,
    jwtCore,
    bCryptScala,
    playAkkaHttpServer,
    playFilterHelpers,
    playSlick,
    postgresql,
    slickPg,
    slickPgCirceJson,
    slickCats,
    scalatestPlusPlay % "test",
    scalaUri,
    tapirEnumeratum,
    tapirJsonCirce,
    tapirOpenApiCirce,
    tapirOpenApiCirceYaml,
    tapirOpenApiDocs,
    tapirPlayServer,
    tapirRedocPlay,
    tapirSwaggerUiPlay,
    testContainersPostgresql % "test",
    testContainersScalaTest  % "test")
}
