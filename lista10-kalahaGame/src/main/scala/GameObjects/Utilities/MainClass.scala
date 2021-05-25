package GameObjects.Utilities

import Actors.{CallbackNextGames, MeasureGameManager}
import GameObjects.AI.evaluation.EvaluationByResult
import GameObjects.AI.minimax.{AlphaBetaVisitor, MiniMaxVisitor}
import akka.actor.{ActorSystem, Props}

class MainClass {
  def init(): Unit = {
    println("avgNodes; avgTime; moves; aiDepth; aiPos")
    val miniMaxVisitor = new AlphaBetaVisitor(new EvaluationByResult)
    val numberOfGame = 5
    val testData = List(
      new TestData(4, miniMaxVisitor, numberOfGame),
      new TestData(5, miniMaxVisitor, numberOfGame),
      new TestData(6, miniMaxVisitor, numberOfGame),
      new TestData(7, miniMaxVisitor, numberOfGame),
      new TestData(8, miniMaxVisitor, numberOfGame),
      new TestData(9, miniMaxVisitor, numberOfGame),
      new TestData(10, miniMaxVisitor, numberOfGame),
    )
    performTest(testData)
  }

  def performTest(testData: List[TestData]): Unit = {
    testData match {
      case Nil => println("end")
      case head::tail =>
        val system = ActorSystem()
        val callback = new CallbackNextGames(head.numberOfGame, this, tail)
        (0 until head.numberOfGame).foreach(_ => {
          ActorSystem().actorOf(Props(classOf[MeasureGameManager], callback, head.visitor, head.aiDepth))
        })
    }
  }
}
