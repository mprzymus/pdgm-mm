package Actors

import GameObjects.AI.MoveDecider
import GameObjects.AI.minimax.{FirstLegalVisitor, FixedDepth, MinMaxAlgorithm, MiniMaxVisitor}
import GameObjects.Outputs.ResultOutput
import GameObjects.Utilities.{Board, Callback, GameFinished, PlayerLower, PlayerPosition, PlayerUpper}
import akka.actor.{Actor, Props}

import scala.util.Random

class MeasureVsFirstLegal(callback: Callback, visitorA: MiniMaxVisitor, firstRandom: Boolean, var depth: Int) extends Actor {
  playGame()

  def getFirstLegal(player: PlayerPosition, board: Board): MinMaxAlgorithm = {
    val visitor = new FirstLegalVisitor
    new MinMaxAlgorithm(board, player, new FixedDepth(2), visitor)
  }

  private def playGame(): Unit = {
    //println("Game started")
    val board = new Board(4)
    val playerA = if (firstRandom) {
      getFirstLegal(PlayerUpper(), board)
    }
    else {
      getAi(PlayerUpper(), board, visitorA)
    }
    val playerB = if (firstRandom) {
      getAi(PlayerLower(), board, visitorA)
    }
    else {
      getFirstLegal(PlayerLower(), board)
    }
    context.actorOf(Props(classOf[Server], playerA, playerB, board, Long.MaxValue: Long, new ResultOutput()))
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
