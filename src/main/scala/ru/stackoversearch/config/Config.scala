package ru.stackoversearch.config

import com.typesafe.config.{Config, ConfigFactory}
import sttp.model.Uri

object Config {

  val config: Config = ConfigFactory.load()

  object Stackoverflow {
    val SOF_URL: Uri     = Uri("api.stackexchange.com").withPath("2.2" :: "search" :: Nil)
    val MAX_REQUEST: Int = 2
  }

  object Server        {
    val SERVER_HOST: String = "localhost"
    val SERVER_PORT: Int    = 8080
  }

}
