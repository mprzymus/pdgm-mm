package GameObjects.AI.minimax

import GameObjects.AI._
import GameObjects.AI.evaluation.EvaluationByResult
import GameObjects.Utilities.{Board, PlayerPosition}

class MinMaxAlgorithm(private val gameBoard: Board, private val aisPosition: PlayerPosition,
                      private val depthDetermination: DepthDetermination, private val aiAlgorithm: MiniMaxVisitor = new MiniMaxVisitor(new EvaluationByResult))
  extends MoveDecider {

  var moves = 0
  var totalNodes = 0

  override def getMove: Int = {
    moves += 1
    aiAlgorithm.counter = 0
    val move = aiAlgorithm.minimax(depthDetermination.determineDepth, aisPosition, gameBoard)
    totalNodes += aiAlgorithm.counter
    //println(s"counter = ${aiAlgorithm.counter}")
    //println(s"Ai choose $move")
    move
  }

  def getNodesAvg: Double = {
    totalNodes.doubleValue / moves.doubleValue()
  }

  def getTimeAvg(time: Long): Double = {
    time.doubleValue / moves.doubleValue
  }

  override def badMoveInform(message: String): Unit = println("ai missed: " + message)
}
