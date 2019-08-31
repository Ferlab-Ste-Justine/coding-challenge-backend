package builder

import model.{TweetHarmonizedCreatedAt, TweetHarmonized, TweetHarmonizedRetweeted, TweetHarmonizedStatusId, TweetHarmonizedText, UserHarmonized, UserHarmonizedId, UserHarmonizedLang, UserHarmonizedName, UserHarmonizedScreenName}
import twitter4j.Status

object TweetHarmonizedBuilder {

  /**
   * condition :
   * - return None if Status is null
   * - return None if user field from Status is null
   * - return None if text field from Status is null
   *
   * this builder reverse the text field from Status as part of Harmonization process
   *
   * @param tweet : tweet Status from twitter4j API
   * @return
   */
  def buildUserHarmonized(tweet: Status): Option[TweetHarmonized] = {

    if(tweet == null || tweet.getUser == null || tweet.getText == null) {
      return None
    }

    val user = tweet.getUser
    val tweetText = tweet.getText

    val userHarmonized = new UserHarmonized(
      UserHarmonizedScreenName(user.getScreenName),
      UserHarmonizedLang(user.getLang),
      UserHarmonizedName(user.getName),
      UserHarmonizedId(user.getId))

    // apply reverse text
    //https://heichwald.github.io/2016/01/17/reverse-words-in-place-scala.html
    val textReverse = (tweetText.reverse.split(" ", -1) map(_.reverse)).mkString(" ")

    Some(new TweetHarmonized(
      TweetHarmonizedText(textReverse),
      TweetHarmonizedCreatedAt(tweet.getCreatedAt),
      TweetHarmonizedRetweeted(tweet.isRetweeted),
      userHarmonized, TweetHarmonizedStatusId(tweet.getId)))
  }
}

