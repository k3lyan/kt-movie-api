package presentation

import play.api.BuiltInComponents
import play.api.db.slick.SlickComponents
import play.core.server.AkkaHttpServerComponents
import play.filters.cors.CORSComponents

trait MovieRegistryServerComponents extends AkkaHttpServerComponents
  with BuiltInComponents
  with CORSComponents
  with SlickComponents