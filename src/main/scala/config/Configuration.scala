package config

import pureconfig._
import pureconfig.generic.auto._

case class Config(appName: String,
                  csvPathFilename: String,
                  sparkStreamTimeWindowSec: Int,
                  twitterAccessToken: String,
                  twitterAccessSecret: String,
                  twitterConsumerKey: String,
                  twitterConsumerSecret: String,
                  twitterFilters: String)

case class PureConfiguration(config: Config)

/**
 * Configuration injection
 */
object Configuration {

  private val configuration = loadConfig[PureConfiguration] match {
    case Right(conf) => conf
    case Left(failures) =>
      throw new Exception(s"Unable to load the configuration ${failures.toList.mkString("\n")}")
  }

  def fetchConfig(): Config = {
    configuration.config
  }
}


