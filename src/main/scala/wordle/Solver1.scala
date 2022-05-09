package wordle

import scala.util.Random

/** Uses a good first word, takes correct letters and previous words (somewhat) into account */
class Solver1 extends Solver {
  def requiredWeights: Int = 0

  override def solveInner(target: String, weights: Seq[Double]): Solution = {
    var next = "least"
    val r = new Random()

    Solution(Range(0, 6).map(_ => {
      val validated = Solver.validate(next, target)

      next = validated.correctCount match {
        case 0 =>
          ValidWordList.randomWord()
        case _ =>
          val randomNext = ValidWordList.words.filter(word => {
            validated.matches(word)
          })
          randomNext(Math.abs(r.nextInt()) % randomNext.length)
      }

      validated
    }))
  }
}
