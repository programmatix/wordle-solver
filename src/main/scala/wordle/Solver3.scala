package wordle

import wordle.Score.normalise

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/** Everything from Solver2 plus:
 * - ranks possible words based on letter frequency & duplicated letters and is more likely to choose a better word
 */
class Solver3 extends Solver {
  val random = new Random()

  def requiredWeights: Int = 2

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
              val scored = words.map(word => Solver3.score(word, weights))
              val best: Score = Score.pickBest(scored)
              best.word
          }
        }
        while (previousWords.contains(next))
      }
    }

    Solution(guesses.toSeq)
  }
}

object Solver3 {
  val LetterFrequencyGoodScore = 50 // based on observation
  val DuplicateLetterBadScore = 2

  def score(word: String, weights: Seq[Double]): Score = {
    val letterFrequencies = LetterFrequencies.rank(word)
    val duplicatedLetters = Constants.Letters - word.toCharArray.distinct.length

    val sc1 = ScoreContributor("frequencies", letterFrequencies, normalise(letterFrequencies, 45), weights(0))
    val sc2 = ScoreContributor("duplicates", duplicatedLetters, -normalise(duplicatedLetters, 2), weights(1))
    val score = sc1.score + sc2.score

    Score(word, Seq(sc1, sc2), score)
  }
}