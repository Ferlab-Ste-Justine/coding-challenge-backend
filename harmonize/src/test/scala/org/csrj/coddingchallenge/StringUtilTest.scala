package org.csrj.coddingchallenge

import org.csrj.coddingchallenge.StringUtil.reverseContent
import org.scalatest.{FlatSpec, Matchers}

class StringUtilTest extends FlatSpec with Matchers{

  "reverseContent" should "reverse an entire sentence" in {
    reverseContent("The lazy fox jumped over the computer.") shouldBe "Computer the over jumped fox lazy the."

  }
  it should "reverse two sentences" in {
    reverseContent("The lazy fox jumped over the computer. This is my sentence.") shouldBe "Sentence my is this. Computer the over jumped fox lazy the."

  }

}
