package model

import java.util.Date

case class TweetHarmonizedText(value: String) extends AnyVal
case class TweetHarmonizedCreatedAt(value: Date) extends AnyVal
case class TweetHarmonizedRetweeted(value: Boolean) extends AnyVal
case class TweetHarmonizedStatusId(value: Long) extends AnyVal

case class UserHarmonizedScreenName(value: String) extends AnyVal
case class UserHarmonizedLang(value: String) extends AnyVal
case class UserHarmonizedName(value: String) extends AnyVal
case class UserHarmonizedId(value: Long) extends AnyVal

class TweetHarmonized(val text: TweetHarmonizedText,
                      val createdAt: TweetHarmonizedCreatedAt,
                      val retweeted : TweetHarmonizedRetweeted,
                      val user: UserHarmonized,
                      val id: TweetHarmonizedStatusId) extends Serializable

class UserHarmonized(val screenName: UserHarmonizedScreenName,
                     val lang: UserHarmonizedLang,
                     val name: UserHarmonizedName,
                     val id: UserHarmonizedId) extends Serializable

