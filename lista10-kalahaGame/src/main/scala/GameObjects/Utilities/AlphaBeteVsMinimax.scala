package GameObjects.Utilities

import Actors.MeasureGameManager
import GameObjects.AI.evaluation.EvaluationByResult
import GameObjects.AI.minimax.AlphaBetaVisitor
import akka.actor.{ActorSystem, Props}

class AlphaBeteVsMinimax {
  def init(): Unit = {
    println("avgNodes; avgTime; moves; aiDepth; aiPos")
    val miniMaxVisitor = new AlphaBetaVisitor(new EvaluationByResult)
    val numberOfGame = 5
    val testData = List(
      new TestData(4, 4, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(5, 5, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(6, 6, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(7, 7, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(8, 8, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(9, 9, miniMaxVisitor, miniMaxVisitor, numberOfGame),
      new TestData(10, 10, miniMaxVisitor, miniMaxVisitor, numberOfGame),
    )
    performTest(testData)
  }

  def performTest(testData: List[TestData]): Unit = {
    testData match {
      case Nil => println("end")
      case head :: tail =>
        val callback = new CallbackNextGames(head.numberOfGame, this, tail)
        (0 until head.numberOfGame).foreach(_ => {
          ActorSystem().actorOf(Props(classOf[MeasureGameManager], callback, head.visitorA, head.visitorB, head.aiADepth))
        })
    }
  }
}
