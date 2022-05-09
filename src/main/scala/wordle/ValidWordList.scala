package wordle

import scala.util.Random

// From https://github.com/nimblebun/wordle-cli/blob/master/words/valid_word_list.go
object ValidWordList {
  val words: Seq[String] = (scala.io.Source.fromFile("src/main/resources/ValidWordList.txt")
    .getLines()
    .flatMap(line => line
      .split(",")
      .map(word => word.trim.replace("\"", "").toUpperCase))
    .toSeq) ++ InterestingWordList.words

  def randomWord(): String = {
    val random = new Random()
    val next = Math.abs(random.nextInt()) % words.length
    words(next)
  }
}
