package wordle

import scala.collection.mutable.ArrayBuffer

/** Looking at how often a letter comes up in a given position.  E.g. 'Y' relatively likely to be last character.
 */
object LetterPositions {
  // For each character, how many times it occurred at position 0, 1, 2...
  val ranking: Map[Char, Seq[Double]] = produceRanking()

  def produceRanking(): Map[Char, Seq[Double]] = {
    val temp = collection.mutable.Map[Char, ArrayBuffer[Double]]()

    for (word <- ValidWordList.words) {
      var idx = 0
      while (idx < word.length) {
        val letter = word(idx)
        if (!temp.contains(letter)) {
          temp(letter) = ArrayBuffer(0,0,0,0,0)
        }
        temp(letter)(idx) += 1
        idx += 1
      }
    }

    temp.map(x => (x._1, x._2.toSeq)).toMap
  }


  def rank(word: String): Double = {
    // Iterative code for performance in hot-spot
    var score = 0.0
    var idx = 0
    while (idx < word.length) {
      val letter = word(idx)
      score += ranking(letter)(idx)
      idx += 1
    }
    score
  }
}
