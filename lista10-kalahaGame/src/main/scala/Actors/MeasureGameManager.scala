package Actors

import GameObjects.AI.MoveDecider
import GameObjects.AI.minimax.{FixedDepth, MinMaxAlgorithm, MiniMaxVisitor}
import GameObjects.Outputs.NoOutput
import GameObjects.Utilities._
import akka.actor.{Actor, Props}

import scala.util.Random

class MeasureGameManager(mainObject: Callback, visitor: MiniMaxVisitor, var depth: Int) extends Actor {
  playGame()
  def changeDepth(): Unit = {
    depth -= 1
  }

  private def playGame(): Unit = {
    //println("Game started")
    val board = new Board(4)
    val playerA = getAi(PlayerUpper(), board)
    val playerB = getAi(PlayerLower(), board)
    firstRandomMove(board)
    context.actorOf(Props(classOf[Server], playerA, playerB, board, Long.MaxValue: Long, NoOutput()))
  }

  private def firstRandomMove(board: Board) = {
    board.move(Random.nextInt(6), PlayerUpper())
  }

  def getAi(position: PlayerPosition, board: Board): MoveDecider = {
    createMinMaxGivenDepth(position, board, depth)
  }

  private def createMinMaxGivenDepth(position: PlayerPosition, board: Board, depth: Int) = {
    new MinMaxAlgorithm(board, position, new FixedDepth(depth), aiAlgorithm = visitor)
  }

  override def receive: Receive = onMessage()

  private def onMessage(): Receive = {
    case GameFinished(_) =>
        context.system.terminate()
        mainObject.callBack()
  }
}
