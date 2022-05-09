package wordle

import wordle.Score.normalise

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/** Everything from Solver3:
 * - weight0: letter frequency
 * - weight1: duplicated letters
 * And adds:
 * - weight2: picks starting word (0.0 = first from list, 1.0 = last)
 * - weight3: how many rounds to use the duplicate letters rule for (0.0 = disabled, 1.0 = all 6).  Since it may be more
 *            useful during opening rounds than later on given the target can contain duplicates
 */
class Solver4 extends Solver {
  val random = new Random()

  def requiredWeights: Int = 4

  override def solveInner(target: String, weights: Seq[Double]): Solution = {
    if (weights.length != 4) throw new IllegalArgumentException("Bad weights")

    val startingWordIndex = (GoodStartingWords.firstWords.length * weights(2)).toInt
    var next = GoodStartingWords.firstWords(startingWordIndex % GoodStartingWords.firstWords.length)
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
              val round = guesses.size
              val scored = words.map(word => Solver4.score(round, word, weights))
              // for debugging val ranked = scored.sortBy(v => - v.score)
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

object Solver4 {
  val LetterFrequencyGoodScore = 50 // based on observation
  val LetterPositionsGoodScore = 11000 // based on calculations
  val DuplicateLetterBadScore = 2

  def score(round: Int, word: String, weights: Seq[Double]): Score = {
    val letterFrequencies = LetterFrequencies.rank(word)
    val sc2 = {
      val useDuplicates = weights(3) != 0.0 && (weights(3) * Constants.AllowedGuesses >= round)
      val duplicatedLetters = Constants.Letters - word.toCharArray.distinct.length
      if (useDuplicates) {
        ScoreContributor("duplicates", duplicatedLetters, -normalise(duplicatedLetters, DuplicateLetterBadScore), weights(1))
      }
      else {
        ScoreContributor("duplicates (disabled)", duplicatedLetters, 0, weights(3))
      }
    }

    val sc1 = ScoreContributor("frequencies", letterFrequencies, normalise(letterFrequencies, LetterFrequencyGoodScore), weights(0))

    val letterPositions = LetterPositions.rank(word)
    val sc3 = ScoreContributor("positions", letterPositions, normalise(letterPositions, LetterPositionsGoodScore), weights(3))

    val score = sc1.score + sc2.score + sc3.score

    Score(word, Seq(sc1, sc2, sc3), score)
  }
}