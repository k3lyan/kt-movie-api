package core

import java.time.LocalDate

final case class MovieInput(title:         String,
                            director:      String,
                            releaseDate:   Option[LocalDate],
                            cast:          Option[String],
                            genre:         Option[String],
                            synopsis:      Option[String])
