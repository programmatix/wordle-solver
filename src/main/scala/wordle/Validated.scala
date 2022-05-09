package wordle

case class Validated(guess: String, letterStates: Seq[LetterState], why: Option[Score] = None) {
  def colourisedGuess: String = {
    guess.toCharArray
      .zipWithIndex
      .map(letter => {
        val ret: String = letterStates(letter._2) match {
          case Correct(l) => Console.GREEN_B + l.toUpper
          case Misplaced(l) => Console.YELLOW_B + l.toUpper
          case _ => Console.RESET + letter._1.toUpper.toString
        }
        ret
      }).mkString + Console.RESET
  }

  def correctCount: Int = {
    letterStates.count(p => p.isInstanceOf[Correct])
  }

  def misplacedCount: Int = {
    letterStates.count(p => p.isInstanceOf[Misplaced])
  }

  def incorrectCount: Int = {
    letterStates.count(p => p.isInstanceOf[Incorrect])
  }

  def solved: Boolean = {
    correctCount == Constants.Letters
  }

  def matches(word: String): Boolean = {
    // Iterative code for performance, replacing previous functional code
    var done = false
    var idx = 0
    var ret = true

    while (!done && idx < letterStates.size) {
      val ls = letterStates(idx)
      ls match {
        case Correct(letter) =>
          ret = word.charAt(idx) == letter
        case Misplaced(letter) =>
          ret = word.contains(letter) && word.charAt(idx) != letter
        case Incorrect(letter) =>
          ret = !word.contains(letter)
      }
      if (!ret) done = true
      idx += 1
    }

    ret
  }

}
