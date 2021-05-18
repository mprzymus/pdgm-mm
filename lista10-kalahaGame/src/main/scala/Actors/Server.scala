package Actors

import akka.actor.{Actor, ActorRef, Props}
import Actors.Player.{AvgResult, MakeMove, Move}
import Actors.Server.{Avg, BadMove}
import GameObjects.AI.MoveDecider
import GameObjects.Outputs.NoOutput
import GameObjects.Outputs.Output
import GameObjects.Utilities._

class Server(playerA: MoveDecider, playerB: MoveDecider, private val board: Board, private val timeForTurn: Long = 10, private val serverOutput: Output = NoOutput()) extends Actor {
  val timer = new Timer()
  val upperChild: ActorRef = context.actorOf(Props(classOf[Player], playerA))
  val lowerChild: ActorRef = context.actorOf(Props(classOf[Player], playerB))

  var lowerTime: Long = 0
  var upperTime: Long = 0

  (if (board.toMove == PlayerUpper()) upperChild else lowerChild) ! MakeMove()

  def addTimeToPlayer(time: Long, player: PlayerPosition): Unit = {
    if (player == PlayerUpper()) {
      upperTime += time
    }
    else if (player == PlayerLower()) {
      lowerTime += time
    }
  }

  override def receive: Receive = {
    case turn: Move =>
      try {
        if (timer.getTimeSeconds < timeForTurn) {
          val time = timer.getTimeMillis
          val player =
            if (sender() == lowerChild) PlayerLower()
            else if (sender() == upperChild) PlayerUpper()
            else throw new InvalidSenderException("Unknown sender")
          addTimeToPlayer(time, player)
          val destination = board.move(turn.house, player)
          if (destination.isInstanceOf[GameFinished])
            finishGame()
          else {
            serverOutput.printGame()
            findDestination(destination) ! MakeMove()
          }
          timer.restart()
        }
        else context.parent ! GameFinished("time out")
      }
      catch {
        case e: IllegalArgumentException =>
          println(e.getMessage)
          serverOutput.printGame()
          sender() ! BadMove()
      }
    case avg: AvgResult =>
      println(s"winner nodes avg    = ${avg.avgNodes}")
      println(s"winner time avg[ms] = ${avg.avgTime}")
      context.parent ! GameFinished()
    case GameFinished(_) =>
      finishGame()
  }

  private def finishGame(): Unit = {
    val message = s"Game finished. Result: ${board.playerUpperScore} - ${board.playerLowerScore}"
    serverOutput.putMessage(message)
    val winner = findDestination(board.winning)
    winner ! Avg(findTime(board.winning))
  }

  private def findTime(player: PlayerPosition): Long = {
    player match {
      case PlayerUpper() => upperTime
      case PlayerLower() => lowerTime
      case GameFinished(_) => throw new UnsupportedOperationException()
    }
  }

  private def findDestination(sendTo: PlayerPosition): ActorRef = {
    sendTo match {
      case PlayerUpper() => upperChild
      case PlayerLower() => lowerChild
    }
  }
}

object Server {

  case class BadMove()

  case class Avg(time: Long)
}