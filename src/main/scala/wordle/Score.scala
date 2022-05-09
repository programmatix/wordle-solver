package wordle

case class ScoreContributor(name: String,
                            // Original raw score, just for debugging
                            raw: Double,
                            // Score normalised to 0 to 1 range
                            adjusted: Double,
                            // Weight given to this score, 0 to 1
                            weight: Double) {
  def score = adjusted * weight

  override def toString: String = f"${name},${raw}%.1f,${adjusted}%.2f,${weight}%.2f,${score}%.2f"
}

case class Score(word: String,
                 contributors: Seq[ScoreContributor],
                 score: Double) {
  override def toString: String = f"${word} ${score}%.2f contrib=${contributors}"
}

object Score {
  def normalise(value: Double, max: Double): Double = {
    Math.min(max, value) / max
  }

  def pickBest(scored: Seq[Score]): Score = {
    var best = scored.head
    // Iterative loop rather than sorting, for performance
    for (n <- scored) {
      if (n.score > best.score) {
        best = n
      }
    }
    best
  }

}