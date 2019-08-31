import builder.TweetHarmonizedBuilder
import config.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import service.WriteOnCSVFileService
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object CodingChallengeBackendMain {

  private val appConfiguration = Configuration.fetchConfig()

  def main(args: Array[String]) {

    val appName = appConfiguration.appName
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(appConfiguration.sparkStreamTimeWindowSec))
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(appConfiguration.twitterConsumerKey)
      .setOAuthConsumerSecret(appConfiguration.twitterConsumerSecret)
      .setOAuthAccessToken(appConfiguration.twitterAccessToken)
      .setOAuthAccessTokenSecret(appConfiguration.twitterAccessSecret)
    val auth = new OAuthAuthorization(cb.build)
    val tweets = TwitterUtils.createStream(ssc, Some(auth), Array(appConfiguration.twitterFilters))

    tweets
      .map(TweetHarmonizedBuilder.buildUserHarmonized)
      .repartition(1)
      .filter(e => e.isDefined)
          .foreachRDD(e => WriteOnCSVFileService.save(e.collect))

    ssc.start()
    ssc.awaitTermination()
  }
}