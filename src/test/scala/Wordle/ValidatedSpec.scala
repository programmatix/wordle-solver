package Wordle

import org.scalatest.funsuite.AnyFunSuite
import wordle.{Correct, Incorrect, LetterState, Misplaced, Solver, Validated}

class ValidatedSpec extends AnyFunSuite {

  def create(guess: String, value: Seq[LetterState]): Validated = {
    Validated(guess, value)
  }

  test("matches guess=STARE target=CIGAR") {
    val v: Validated = Solver.validate("STARE", "CIGAR")
    assert(!v.matches("ABEAR"))
    assert(v.matches("AARGH"))
  }

}
