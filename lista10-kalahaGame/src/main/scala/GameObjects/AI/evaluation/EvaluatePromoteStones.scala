package GameObjects.AI.evaluation
import GameObjects.Utilities.{Board, PlayerPosition}

class EvaluatePromoteStones extends EvaluationStrategy {
  val evaluationByResult = new EvaluationByResult

  override def evaluate(player: PlayerPosition, board: Board): Int = {
    val diff = evaluationByResult.evaluate(player, board)
    val stones = board.sumStonesInHouses(player)
    diff + stones
  }
}
