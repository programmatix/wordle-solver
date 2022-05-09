package wordle

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/** Everything from Solver1 plus:
 * - takes incorrect positions into account
 * - takes previous guesses into account
 * - better starting word choices
 * - stops on success
 * - skips already used words
 */
class Solver2 extends Solver {
  val random = new Random()

  def requiredWeights: Int = 0

  override def solveInner(target: String, weights: Seq[Double]): Solution = {
    var next = GoodStartingWords.bestDefault
    // Doing a non-functional style so we can easily look at previous guesses
    val guesses = new ArrayBuffer[Validated]()
    var words = ValidWordList.words

    while (guesses.size < Constants.AllowedGuesses && !solved(guesses)) {
      val mostRecentGuess = Solver.validate(next, target)
      guesses.append(mostRecentGuess)

      if (!mostRecentGuess.solved) {
        val previousWords = guesses.map(v => v.guess)

        do {
          next = mostRecentGuess.incorrectCount match {
            case Constants.Letters =>
              GoodStartingWords.randomFirstWord
            case _ =>
              // As an optimisation, hold onto the words each iteration, rather than calculating from scratch all words
              // that match all prior guesses.
              words = words.filter(word => mostRecentGuess.matches(word))
              words(Math.abs(random.nextInt()) % words.length)
          }
        }
        while (previousWords.contains(next))
      }
    }

    Solution(guesses.toSeq)
  }
}
