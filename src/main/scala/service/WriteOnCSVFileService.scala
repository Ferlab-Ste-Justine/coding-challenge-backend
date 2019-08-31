package service

import java.nio.file.{Files, Paths}
import java.util

import config.Configuration
import model.TweetHarmonized

import scala.io.Source

object WriteOnCSVFileService {

  private val appConfiguration = Configuration.fetchConfig()
  private val fileName = appConfiguration.csvPathFilename
  private val csvHeader = "UID,Screen Name,Language,Content"
  private val csvFileBuffered = Source.fromFile(fileName)

  /**
   * tweet order map to keep tweet record sorted
   * key=screen name (lowercase)
   * value=actual string value for csv
   */
  var orderedTweetTreeMap = new util.TreeMap[String, String]

  //initialize tweet cache by reading csv file
  {
    println(s"fileName=$fileName")
    csvFileBuffered.getLines.toList.filter(e => e != csvHeader)
      .foreach(e => {
        // get screen name from all records loaded from files
        orderedTweetTreeMap.put(e.split(",").apply(1).toLowerCase(), e)
      })
    csvFileBuffered.close()
  }

  /**
   * We keep tweet Status always sorted in TreeSet Data Structure And ovewrite csv file ever time
   * this function is called
   * @param arrayOptionTweetHarmonizedEntity : Optional Array of tweet
   */
  def save(arrayOptionTweetHarmonizedEntity: Array[Option[TweetHarmonized]]): Unit = {
    arrayOptionTweetHarmonizedEntity.flatten
      .toList
      .foreach(tweetHarmonized => {
        orderedTweetTreeMap.put(
          tweetHarmonized.user.screenName.value.toLowerCase(),
          s"${tweetHarmonized.id.value}," +
            s"${tweetHarmonized.user.screenName.value}," +
            s"${tweetHarmonized.user.lang.value}," +
            s"${tweetHarmonized.text.value.replaceAll("\\n", "")}"
        )
      })

    //add csv header
    orderedTweetTreeMap.put("", csvHeader)
    Files.write(Paths.get(fileName), orderedTweetTreeMap.values())
  }

  /**
   * useful for unit test setup WriteOnCSVFileServiceTest
   */
  def clearCache():Unit ={
    orderedTweetTreeMap.clear()
  }
}
