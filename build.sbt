import scala.sys.process._

lazy val `kt-movie-api` = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "kt-movie-api",
    scalaVersion := Version.scala,
    //==================================================================================================================
    // BuildInfo
    //==================================================================================================================
    buildInfoKeys ++= Seq[BuildInfoKey](
      "gitBranch" -> "git rev-parse --abbrev-ref HEAD".!!.trim,
      "buildDate" -> java.time.LocalDateTime.now),
    //==================================================================================================================
    // Compiler
    //==================================================================================================================
    scalacOptions ++= Seq("-feature", "-language:higherKinds"),
    Test / scalacOptions ++= Seq("-Yrangepos"),
    //==================================================================================================================
    // Dependencies
    //==================================================================================================================
    libraryDependencies ++= Dependencies.ktMovieApi,
    //==================================================================================================================
    // Test
    //==================================================================================================================
    Test / parallelExecution := true)