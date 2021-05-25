package Actors

import GameObjects.AI.MoveDecider
import GameObjects.AI.minimax.{FixedDepth, MinMaxAlgorithm, MiniMaxVisitor}
import GameObjects.Outputs.{ConsoleOutput, NoOutput, ResultOutput}
import GameObjects.Utilities._
import akka.actor.{Actor, Props}

import scala.util.Random

class MeasureGameManager(callback: Callback, visitorA: MiniMaxVisitor, visitorB: MiniMaxVisitor, var depth: Int) extends Actor {
  playGame()

  private def playGame(): Unit = {
    //println("Game started")
    val board = new Board(4)
    val playerA = getAi(PlayerUpper(), board, visitorA)
    val playerB = getAi(PlayerLower(), board, visitorB)
    context.actorOf(Props(classOf[Server], playerA, playerB, board, Long.MaxValue: Long, new ResultOutput()))
  }

  private def firstRandomMove(board: Board) = {
    board.move(Random.nextInt(6), PlayerUpper())
  }

  def getAi(position: PlayerPosition, board: Board, visitor: MiniMaxVisitor): MoveDecider = {
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
