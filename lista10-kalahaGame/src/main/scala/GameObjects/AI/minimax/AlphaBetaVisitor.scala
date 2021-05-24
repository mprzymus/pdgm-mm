package GameObjects.AI.minimax

import GameObjects.AI.evaluation.EvaluationStrategy
import GameObjects.Utilities.{Board, PlayerPosition}

import scala.collection.mutable.ListBuffer

class AlphaBetaVisitor(override val evaluationStrategy: EvaluationStrategy, override val nodeChildrenNumber: Int = 5)
  extends MiniMaxVisitor(evaluationStrategy, nodeChildrenNumber) {
  override val defaultBestBrother: Int = Int.MinValue

  override protected def iterateOverPossibilities(depth: Int, player: PlayerPosition, board: Board, alpha: Int, beta: Int): Seq[Int] = {
    var alphaMutable = alpha
    var betaMutable = beta
    val list = 0 to nodeChildrenNumber
    val poss = ListBuffer.empty[Int]
    poss += Int.MinValue
    if (player == maxPlayerPosition) {
      for (child <- list) {
        val newValue = minimax(depth, player, board.clone(), child, alphaMutable, betaMutable)
        if (newValue != Int.MinValue) {
          alphaMutable = Math.max(newValue, alphaMutable)
          if (alphaMutable >= betaMutable) {
            poss += newValue
            return List(alphaMutable)
          } else poss += newValue
        }
      }
    }
    else {
      for (child <- list) {
        val newValue = minimax(depth, player, board.clone(), child, alphaMutable, betaMutable)
        if (newValue != Int.MinValue) {
          betaMutable = Math.min(-newValue, betaMutable)
          if (alphaMutable >= betaMutable) {
            poss += newValue
            return List(newValue)
          } else poss += newValue
        }
      }
    }
    poss.result()
  }
}

//brać element po elemencie
