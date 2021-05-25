package GameObjects.AI.minimax

import GameObjects.AI.evaluation.{EvaluationByResult, EvaluationStrategy}
import GameObjects.Utilities.{Board, PlayerPosition}

class FirstLegalVisitor(override val evaluationStrategy: EvaluationStrategy = new EvaluationByResult(), override val nodeChildrenNumber: Int = 5)
  extends MiniMaxVisitor(evaluationStrategy, nodeChildrenNumber) {
  override def minimax(depth: Int, player: PlayerPosition, board: Board): Int = {
    val children = checkChildren(1, player, board)
    children.indexWhere(element => {
      element != Int.MinValue
    }
    )
  }
}
