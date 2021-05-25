package GameObjects.AI.evaluation

import GameObjects.Utilities.{Board, PlayerPosition}

class EvaluationPromoteEmptyPits extends EvaluationStrategy {
  val evaluationByResult = new EvaluationByResult

  override def evaluate(player: PlayerPosition, board: Board): Int = {
    val diff = evaluationByResult.evaluate(player, board)
    val emptyStones = board.playerPits(player).foldLeft(0) {
      (acc, value) =>
        if (value == 0) {
          acc + 1
        }
        else {
          acc
        }
    }
    diff + emptyStones
  }
}
