package Actors

import GameObjects.AI.MoveDecider
import GameObjects.AI.minimax.{FixedDepth, MinMaxAlgorithm, MiniMaxVisitor}
import GameObjects.Outputs.{NoOutput, ResultOutput}
import GameObjects.Utilities._
import akka.actor.{Actor, Props}

class MeasureGameManager(callback: Callback, visitorA: MiniMaxVisitor, visitorB: MiniMaxVisitor, var depthA: Int, val depthB: Int) extends Actor {
  playGame()

  private def playGame(): Unit = {
    val board = new Board(4)
    val playerA = getAi(PlayerUpper(), board, visitorA, depthA)
    val playerB = getAi(PlayerLower(), board, visitorB, depthB)
    context.actorOf(Props(classOf[Server], playerA, playerB, board, Long.MaxValue: Long, new ResultOutput()))
  }

  def getAi(position: PlayerPosition, board: Board, visitor: MiniMaxVisitor,depth: Int): MoveDecider = {
    createMinMaxGivenDepth(position, board, depth, visitor)
  }

  private def createMinMaxGivenDepth(position: PlayerPosition, board: Board, depth: Int, visitor: MiniMaxVisitor) = {
    new MinMaxAlgorithm(board, position, new FixedDepth(depth), aiAlgorithm = visitor)
  }

  override def receive: Receive = onMessage()

  private def onMessage(): Receive = {
    case GameFinished(_) =>
      context.system.terminate()
      callback.callBack()
  }
}
