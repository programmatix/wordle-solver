package wordle

import java.io.{File, FileWriter}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class Attempt(target: String, weights: Seq[Double], solution: Solution)

/** Tries various weights against a solver */
class MetaSolver {
  def improve(solver: Solver, output: String): Unit = {
    val random = new Random()
    val fw = new FileWriter(output, true) ;

    while (true) {

      val weights = Range(0, solver.requiredWeights).map(_ => random.nextDouble())
      val attempts = new ArrayBuffer[Attempt]()

      val randomWords = random.shuffle(ValidWordList.words).take(1000)
      randomWords.foreach(target => {
        val solution = solver.solve(target, weights)
        //        solution.solution.zipWithIndex
        //          .foreach(s => println(s"${s._2} ${s._1.colourisedGuess} ${target.toUpperCase}"))
          attempts.append(Attempt(target, weights, solution))
      })

      val totalAttempts = attempts.foldLeft(0)((acc, next) => acc + next.solution.solution.size)
      println(s"Total: ${weights} ${totalAttempts}")
      fw.write(s"${weights.mkString(",")},${totalAttempts}\n")
      fw.flush()
    }

    fw.close()
  }
}
