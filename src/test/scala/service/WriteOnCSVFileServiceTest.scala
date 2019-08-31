package service

import java.io.File
import java.util.Date

import model.{TweetHarmonized, TweetHarmonizedCreatedAt, TweetHarmonizedRetweeted, TweetHarmonizedStatusId, TweetHarmonizedText, UserHarmonized, UserHarmonizedId, UserHarmonizedLang, UserHarmonizedName, UserHarmonizedScreenName}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.io.Source


class WriteOnCSVFileServiceTest extends FunSuite with BeforeAndAfter {
  val csvFilePath = "src/test/resources/test-database.csv"

  /**
   * delete and create new csv file to setup unit test
   * clear TreeSet Data Structure
   */
  before {
    val file = new File(csvFilePath)
    file.delete()
    file.createNewFile()
    WriteOnCSVFileService.clearCache()
  }

  test("WriteOnCSVFileService should write 1 tweet data to csv file when 1 tweet is sent") {

    val tweeUserHarmonized = new UserHarmonized(UserHarmonizedScreenName("screen_name1"),
      UserHarmonizedLang("lang1"),
      UserHarmonizedName("name1"),
      UserHarmonizedId(10000001))

    val now = new Date()
    val tweeHarmonized = new TweetHarmonized(TweetHarmonizedText("Hello my text 11"),
      TweetHarmonizedCreatedAt(now),
      TweetHarmonizedRetweeted(true),
      tweeUserHarmonized,
      TweetHarmonizedStatusId(121231445))

    WriteOnCSVFileService.save(Array(Some(tweeHarmonized)))

    val csvFileSource = Source.fromFile(csvFilePath)
    val csvFileContent = csvFileSource.getLines.toList
    csvFileSource.close()

    assert(csvFileContent.head == "UID,Screen Name,Language,Content")
    assert(csvFileContent.apply(1) == "121231445,screen_name1,lang1,Hello my text 11")
    assert(csvFileContent.size == 2)
  }

  /**
   * known issue : a unique user (screen_name) can only have one tweet inside csv
   * because of the data structure used to track the order (TreeSet)
   */
  test("WriteOnCSVFileService should write the last saved tweet data from same user to csv file") {

    val tweeUserHarmonized = new UserHarmonized(UserHarmonizedScreenName("screen_name1"),
      UserHarmonizedLang("lang1"),
      UserHarmonizedName("name1"),
      UserHarmonizedId(10000001))

    val now = new Date()
    val tweeHarmonized1 = new TweetHarmonized(TweetHarmonizedText("Hello my text 11"),
      TweetHarmonizedCreatedAt(now),
      TweetHarmonizedRetweeted(true),
      tweeUserHarmonized,
      TweetHarmonizedStatusId(121231445))

    val tweeHarmonized2 = new TweetHarmonized(TweetHarmonizedText("Hello my text 22"),
      TweetHarmonizedCreatedAt(now),
      TweetHarmonizedRetweeted(true),
      tweeUserHarmonized,
      TweetHarmonizedStatusId(999999))

    WriteOnCSVFileService.save(Array(Some(tweeHarmonized1), Some(tweeHarmonized2)))

    val csvFileSource = Source.fromFile(csvFilePath)
    val csvFileContent = csvFileSource.getLines.toList
    csvFileSource.close()

    assert(csvFileContent.head == "UID,Screen Name,Language,Content")
    assert(csvFileContent.apply(1) == "999999,screen_name1,lang1,Hello my text 22")
    assert(csvFileContent.size == 2)
  }

  test("WriteOnCSVFileService save should write nothing when None is pass as argument") {

    WriteOnCSVFileService.save(Array(None))

    val csvFileSource = Source.fromFile(csvFilePath)
    val csvFileContent = csvFileSource.getLines.toList
    csvFileSource.close()

    assert(csvFileContent.head == "UID,Screen Name,Language,Content")
    assert(csvFileContent.size == 1)
  }
}
