package GameObjects.AI.minimax

import GameObjects.AI.evaluation.EvaluationStrategy
import GameObjects.Utilities.{Board, PlayerPosition}

class AlphaBetaVisitor(override val evaluationStrategy: EvaluationStrategy, override val nodeChildrenNumber: Int = 5)
  extends MiniMaxVisitor(evaluationStrategy, nodeChildrenNumber) {
  override val defaultBestBrother: Int = Int.MinValue

  override protected def iterateOverPossibilities(depth: Int, player: PlayerPosition, board: Board, alpha: Int, beta: Int): Seq[Int] = {
    val result = (0 to nodeChildrenNumber).map{ child =>
      val newValue = minimax(depth, player, board.clone(), child, alpha, beta)
      if (player == maxPlayerPosition) {
        val newBeta = Math.min(newValue, beta)
        if (alpha >= newBeta)
          cut
        else newValue
      }
      else {
        val newAlpha = Math.max(newValue, alpha)
        if (newAlpha >= beta)
          cut
        else newValue
      }
    }
    result
  }

  private def cut = {
    //println("cut")
    Int.MinValue
  }

  def isBetter(acc: Int, bestBrother: Int): Boolean = acc < bestBrother
}
