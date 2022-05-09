package wordle

import scala.util.Random

case class Solution(solution: Seq[Validated])

object Main {
  def main(args: Array[String]): Unit = {
    val meta = new MetaSolver
    val solver = new Solver4()
    meta.improve(solver, "solver4.csv")
  }
}

object OneSolver {
  def main(args: Array[String]): Unit = {
    val target = InterestingWordList.randomWord
    val solver = new Solver4()
    val random = new Random()
    val weights = Range(0, solver.requiredWeights).map(_ => random.nextDouble())
    val solution = solver.solve(target, weights)
    solution.solution.zipWithIndex
//      .foreach(s => println(s"${s._2} ${s._1.colourisedGuess} ${s._1.why.getOrElse("-")}"))
      .foreach(s => println(s"${s._2} ${s._1.colourisedGuess} ${s._1.why.getOrElse("-")}"))
  }
}





