package Wordle

import org.scalatest.funsuite.AnyFunSuite
import wordle.{Correct, Incorrect, LetterState, Misplaced, Solver, Validated}

class ValidatedSpec extends AnyFunSuite {

  def create(guess: String, value: Seq[LetterState]): Validated = {
    Validated(guess, value)
  }

  test("matchesAllCorrectLetters") {
    val v: Validated = create("lea", Seq(Incorrect('l'), Correct('e'), Correct('a')))
    assert(v.matchesAllCorrectLetters("xea"))
    assert(!v.matchesAllCorrectLetters("xeb"))
  }

  test("matchesAllMisplacedLetters") {
    val v: Validated = create("lea", Seq(Incorrect('l'), Misplaced('e'), Incorrect('a')))
    assert(!v.matchesAllMisplacedLetters("lea"))
    assert(v.matchesAllMisplacedLetters("ela"))
    assert(v.matchesAllMisplacedLetters("ela"))
  }

  test("matchesAllMisplacedLetters guess=STARE target=CIGAR") {
    val v: Validated = Solver.validate("STARE", "CIGAR")
    assert(!v.matchesAllMisplacedLetters("STARE"))
    assert(!v.matchesAllMisplacedLetters("LANCE"))
    assert(v.matchesAllMisplacedLetters("CIGAR"))
  }

  test("matches guess=STARE target=CIGAR") {
    val v: Validated = Solver.validate("STARE", "CIGAR")
    assert(!v.matches("ABEAR"))
    assert(v.matches("AARGH"))
  }

}
