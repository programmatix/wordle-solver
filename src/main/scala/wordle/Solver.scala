package wordle

trait Solver {
  def requiredWeights: Int

  /**
   * @param target the word this solver is trying to reach
   * @param weights always between 0 and 1.  Different solvers have different numbers of weights and give them different meanings
   */
  def solve(target: String, weights: Seq[Double]): Solution = {
    val solution = solveInner(target, weights)

    // Eliminate any guesses after the first solution
    val firstSolvedAt = solution.solution
      .zipWithIndex
      .find(p => p._1.solved)

    firstSolvedAt match {
      case Some((_, index)) => Solution(solution.solution.zipWithIndex.filter(x => x._2 <= index).map(x => x._1))
      case _ => solution
    }
  }

  def solveInner(target: String, weights: Seq[Double]): Solution

  def solved(guesses: Iterable[Validated]): Boolean = {
    guesses.nonEmpty && guesses.last.solved
  }
}

object Solver {
  def validate(guess: String, target: String): Validated = {
    val letterStates: Array[LetterState] = guess.toCharArray
      .zipWithIndex
      .map(x => {
        val index = x._2
        val letter = x._1
        val targetLetter = target.charAt(index)

        if (letter == targetLetter) Correct(letter)
        else if (target.contains(letter)) Misplaced(letter)
        else Incorrect(letter)
      })

    Validated(guess, letterStates)
  }
}