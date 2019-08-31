package builder

import java.util.Date

import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import twitter4j.{Status, User}

class TweetHarmonizedBuilderTest extends FunSuite with MockFactory{
  val now = new Date()

  test("TweetHarmonizedBuilder should return an TweetHarmonized Happy Path") {

    val mockUserTwitter = mock[User]
    (mockUserTwitter.getScreenName _).expects().returning("screenName001").atLeastOnce()
    (mockUserTwitter.getLang _).expects().returning("lang001").atLeastOnce()
    (mockUserTwitter.getName _).expects().returning("cricri").atLeastOnce()
    (mockUserTwitter.getId _).expects().returning(102304).atLeastOnce()

    val mockStatusTwitter = mock[Status]
    (mockStatusTwitter.getUser _).expects().returning(mockUserTwitter).atLeastOnce()
    (mockStatusTwitter.getId _).expects().returning(10001).atLeastOnce()
    (mockStatusTwitter.isRetweeted _).expects().returning(true).atLeastOnce()
    (mockStatusTwitter.getText _).expects().returning("hello bla blu blo").atLeastOnce()
    (mockStatusTwitter.getCreatedAt _).expects().returning(now).atLeastOnce()

    val tweetHarmonized = TweetHarmonizedBuilder.buildUserHarmonized(mockStatusTwitter)

    assert(tweetHarmonized.get.id.value == 10001)
    assert(tweetHarmonized.get.retweeted.value)
    assert(tweetHarmonized.get.text.value == "blo blu bla hello")
    assert(tweetHarmonized.get.createdAt.value.equals(now))

    assert(tweetHarmonized.get.user.id.value == 102304)
    assert(tweetHarmonized.get.user.lang.value == "lang001")
    assert(tweetHarmonized.get.user.name.value == "cricri")
    assert(tweetHarmonized.get.user.screenName.value == "screenName001")
  }

  test("TweetHarmonizedBuilder should return None if Twitter User is null") {
    val mockStatusTwitter = mock[Status]
    (mockStatusTwitter.getUser _).expects().returning(null)

    val emptyHarmonized = TweetHarmonizedBuilder.buildUserHarmonized(mockStatusTwitter)

    assert(emptyHarmonized.isEmpty)
  }

  test("TweetHarmonizedBuilder should return None if Twitter Status.text is null") {
    val mockUserTwitter = mock[User]
    val mockStatusTwitter = mock[Status]

    (mockStatusTwitter.getUser _).expects().returning(mockUserTwitter).atLeastOnce()
    (mockStatusTwitter.getText _).expects().returning(null).atLeastOnce()

    val emptyHarmonized = TweetHarmonizedBuilder.buildUserHarmonized(mockStatusTwitter)

    assert(emptyHarmonized.isEmpty)
  }

  test("TweetHarmonizedBuilder should raise an exception if Twitter Status is null") {
    val emptyHarmonized = TweetHarmonizedBuilder.buildUserHarmonized(null)

    assert(emptyHarmonized.isEmpty)
  }
}
