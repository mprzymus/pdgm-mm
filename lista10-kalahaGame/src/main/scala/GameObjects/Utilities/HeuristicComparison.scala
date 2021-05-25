package GameObjects.Utilities

import Actors.MeasureGameManager
import GameObjects.AI.evaluation.{EvaluatePromoteStones, EvaluationByResult, EvaluationPromoteEmptyPits}
import GameObjects.AI.minimax.AlphaBetaVisitor
import akka.actor.{ActorSystem, Props}

class HeuristicComparison {

  def init(): Unit = {
    val diffVisitor = new AlphaBetaVisitor(new EvaluationByResult)
    val stonesVisitor = new AlphaBetaVisitor(new EvaluatePromoteStones)
    val emptyVisitor = new AlphaBetaVisitor(new EvaluationPromoteEmptyPits)
    val numberOfGame = 5
    val depth = 11
    val testData = List(
      new TestData(depth, diffVisitor, stonesVisitor, numberOfGame),
      new TestData(depth, stonesVisitor, diffVisitor, numberOfGame),
      new TestData(depth, diffVisitor, emptyVisitor, numberOfGame),
      new TestData(depth, emptyVisitor, diffVisitor, numberOfGame),
      new TestData(depth, emptyVisitor, stonesVisitor, numberOfGame),
      new TestData(depth, stonesVisitor, emptyVisitor, numberOfGame),
    )
    performTest(testData)
  }

  def performTest(testData: List[TestData]): Unit = {
    testData match {
      case Nil => println("end")
      case head :: _ =>
        val callback = new HeuristicsCallback(testData, this)
        println(s"${head.visitorA.evaluationStrategy.getClass.getSimpleName} vs ${head.visitorB.evaluationStrategy.getClass.getSimpleName}")
        ActorSystem().actorOf(Props(classOf[MeasureGameManager], callback, head.visitorA, head.visitorB, head.aiDepth))
    }
  }
}
