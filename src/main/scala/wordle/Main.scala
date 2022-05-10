package wordle

import scala.util.Random
import com.monovore.decline._
import cats.implicits._

case class Solution(solution: Seq[Validated])

//object Main {
//  def main(args: Array[String]): Unit = {
//    val meta = new MetaSolver
//    val solver = new Solver4()
//    meta.improve(solver, "solver4.csv")
//  }
//}

object OneSolver extends CommandApp(
  name = "wordle-solver",
  header = "Says hello!",
  main = {
    val targetOpt =
      Opts.option[String]("target", help = "Target word to solve - try putting in today's Wordle solution and see if you beat it.  Leave empty to choose a random word").withDefault("random")

    val debugOpt =
      Opts.flag("verbose", help = "Verbose diagnostics").orFalse

    (targetOpt, debugOpt).mapN { (targetRaw, debug) => {
      val target = if (targetRaw == "random") InterestingWordList.randomWord else targetRaw
      val solver = new Solver4()
      val random = new Random()
      val weights = Range(0, solver.requiredWeights).map(_ => random.nextDouble())
      val solution = solver.solve(target, weights)
      solution.solution.zipWithIndex
        .foreach(s => println(s"${s._2} ${s._1.colourisedGuess} ${if (debug) s._1.why.getOrElse("-") else ""}"))
    }}
  })





