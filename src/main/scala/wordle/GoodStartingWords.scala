package wordle

import scala.util.Random

object GoodStartingWords {
  /** Sources:
   * https://www.tomsguide.com/uk/news/best-wordle-start-words
   * WordleBot
   */
  val firstWords = Seq("least", "aisle", "soare", "roate", "raise", "seare", "stare", "adieu", "audio", "about", "canoe",
    "crane", "slate", "crate", "slant", "trace", "lance", "carte", "least", "trice", "roast").map(word => word.toUpperCase)


  // Source: https://www.tomsguide.com/uk/news/best-wordle-start-words
  val bestDefault = "STARE"

  def randomFirstWord = firstWords(Math.abs(new Random().nextInt()) % firstWords.length)
}
