package model

import java.util.Date

import org.scalatest.FunSuite

class TweeHarmonizedTest extends FunSuite {

  val now = new Date()
  test("TweeHarmonized should return an Object once it is instanciated (Happy Path)") {
    val userHarmonized = new UserHarmonized(
      UserHarmonizedScreenName("screen_name1"),
      UserHarmonizedLang("lang1"),
      UserHarmonizedName("name1"),
      UserHarmonizedId(10023))

    val tweetHarmonized = new TweetHarmonized(
      TweetHarmonizedText("text blub"),
      TweetHarmonizedCreatedAt(now),
      TweetHarmonizedRetweeted(true),
      userHarmonized,
      TweetHarmonizedStatusId(1012324))

    assert(tweetHarmonized.createdAt.value == now)
    assert(tweetHarmonized.text.value == "text blub")
    assert(tweetHarmonized.retweeted.value)
    assert(tweetHarmonized.user.equals(userHarmonized))
    assert(tweetHarmonized.id.value == 1012324)
  }


  test("UserHarmonized should return an Object once it is instanciated Happy Path") {
    val userHarmonized = new UserHarmonized(
      UserHarmonizedScreenName("screen_name1"),
      UserHarmonizedLang("lang1"),
      UserHarmonizedName("name1"),
      UserHarmonizedId(100223))

    assert(userHarmonized.screenName.value == "screen_name1")
    assert(userHarmonized.name.value == "name1")
    assert(userHarmonized.lang.value == "lang1")
    assert(userHarmonized.id.value == 100223)

  }
}