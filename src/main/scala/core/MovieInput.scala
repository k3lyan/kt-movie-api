package core

final case class MovieInput(title:         String,
                            director:      String,
                            releaseDate:   Option[ReleaseDate],
                            cast:          Option[String],
                            genre:         Option[String],
                            synopsis:      Option[String])
