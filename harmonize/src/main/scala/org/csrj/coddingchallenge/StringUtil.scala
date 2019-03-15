package org.csrj.coddingchallenge

object StringUtil {
  def reverseContent(content: String): String = {
    val reversedSentences = content.split("\\.").map { sentence: String =>
      val words = sentence.trim.split(" ")
      val reversed = words.reverse
      reversed.mkString(" ").toLowerCase.capitalize

    }
    reversedSentences.reverse.mkString(". ") + "."
  }
}
