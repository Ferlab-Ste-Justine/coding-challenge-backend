package config

import org.scalatest.FunSuite

class ConfigurationTest extends FunSuite {

  private val configTest = Configuration.fetchConfig()

  test("configuration validation") {
    assert(configTest.appName == "coding-challenge-test")
    assert(configTest.csvPathFilename == "src/test/resources/test-database.csv")
    assert(configTest.sparkStreamTimeWindowSec == 5)
    assert(configTest.twitterAccessToken == "blub-access-token")
    assert(configTest.twitterAccessSecret == "blub-access-secret")
    assert(configTest.twitterConsumerKey == "blub-consumer-key")
    assert(configTest.twitterConsumerSecret == "blub-consumer-secret")
    assert(configTest.twitterFilters == "dog")
  }
}
