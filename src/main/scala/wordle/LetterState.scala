package wordle

sealed trait LetterState {
  val letter: Char
}
case class Correct(letter: Char) extends LetterState

case class Misplaced(letter: Char) extends LetterState

case class Incorrect(letter: Char) extends LetterState

