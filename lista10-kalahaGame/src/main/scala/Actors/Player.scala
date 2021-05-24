package Actors

import Actors.Player.{AvgResult, MakeMove, Move}
import Actors.Server.{Avg, BadMove}
import GameObjects.AI.MoveDecider
import GameObjects.AI.minimax.MinMaxAlgorithm
import akka.actor.Actor

class Player(val playerDecider: MoveDecider) extends Actor {
  override def receive: Receive = {
    case MakeMove() =>
      makeMove()
    case BadMove() =>
      makeMove()
    case Avg(time) =>
      playerDecider match {
        case algorithm: MinMaxAlgorithm =>
          context.parent ! AvgResult(algorithm.getNodesAvg, algorithm.getTimeAvg(time), algorithm.moves)
        case _ => throw new UnsupportedOperationException()
      }
  }

  private def makeMove(): Unit = {
    try {
      val move = Move(playerDecider.getMove)
      context.parent ! move
    }
    catch {
      case e: IllegalAccessException =>
        playerDecider.badMoveInform(e.getMessage)
        throw e
    }
  }
}

object Player {

  case class MakeMove()
  case class AvgResult(avgNodes: Double, avgTime: Double, moves: Int)
  case class Move(house: Int)
}
