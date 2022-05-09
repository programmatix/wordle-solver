package wordle

/** Picks a totally random word each time */
class SolverRandom extends Solver {
  override def solveInner(target: String, weights: Seq[Double]): Solution = {
    Solution(Range(0, 6).map(_ => {
      val guess = ValidWordList.randomWord()
      val validated = Solver.validate(guess, target)
      validated
    }))
  }

  override def requiredWeights: Int = 0
}
