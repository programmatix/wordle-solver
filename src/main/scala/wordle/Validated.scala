package wordle

case class Validated(guess: String, letterStates: Seq[LetterState]) {
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

  def matchesAllCorrectLetters(word: String): Boolean = {
    if (correctCount == 0) true
    else {
      val r = word.toCharArray
        .zipWithIndex
        .map(x => {
          val ls = letterStates(x._2)
          ls match {
            case Correct(l) => l == x._1
            case _ => true
          }
        })
      // If any are false, return false
      r.find(p => !p).getOrElse(true)
    }
  }

  /** Want the misplaced letters to be in `word`, but not in the same places
   * The original functional implementation.  For performance, had to be replaced with an iterative version.
   */
  def matchesAllMisplacedLettersFunctional(word: String): Boolean = {
    if (misplacedCount == 0) true
    else {
      val r: Seq[Boolean] = letterStates
        .zipWithIndex
        .filter(v => v._1.isInstanceOf[Misplaced])
        .map(v => word.contains(v._1.letter) && word.charAt(v._2) != v._1.letter)
      // If any are false, return false
      r.find(p => !p).getOrElse(true)
    }
  }

  def matchesAllMisplacedLetters(word: String): Boolean = {
    // Iterative code for performance, replacing matchesAllMisplacedLettersFunctional
    var done = false
    var idx = 0
    var ret = true

    while (!done && idx < letterStates.size) {
      val ls = letterStates(idx)
      ls match {
        case Misplaced(letter) =>
          ret = word.contains(letter) && word.charAt(idx) != letter
          if (!ret) done = true
        case _ =>
      }
      idx += 1
    }

    ret
  }

  /** `word` cannot have any of our incorrect letters.
   * Return true if that's the case (everything's fine) */
  def matchesAllIncorrectLettersFunctional(word: String): Boolean = {
    if (incorrectCount == 0) true
    else {
      val foundIncorrectLetterInWord: Seq[Boolean] = letterStates
        .zipWithIndex
        .filter(v => v._1.isInstanceOf[Incorrect])
        .map(v => word.contains(v._1.letter))
      // If any are true, return false
      !foundIncorrectLetterInWord.find(p => p).getOrElse(false)
    }
  }

  def matchesAllIncorrectLetters(word: String): Boolean = {
    // Iterative code for performance, replacing matchesAllIncorrectLettersFunctional
    var done = false
    var idx = 0
    var ret = true

    while (!done && idx < letterStates.size) {
      val ls = letterStates(idx)
      ls match {
        case Incorrect(letter) =>
          ret = !word.contains(letter)
          if (!ret) done = true
        case _ =>
      }
      idx += 1
    }

    ret
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
