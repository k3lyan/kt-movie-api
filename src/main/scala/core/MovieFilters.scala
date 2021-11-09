package core

import java.time.LocalDate

final case class MovieFilters(title: String,
                              date: Option[LocalDate],
                              genre: Option[String])
