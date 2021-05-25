package GameObjects.Utilities.heursitics

import Actors.MeasureGameManager
import GameObjects.AI.evaluation.{EvaluatePromoteStones, EvaluationByResult, EvaluationPromoteEmptyPits}
import GameObjects.AI.minimax.AlphaBetaVisitor
import GameObjects.Utilities.TestData
import akka.actor.{ActorSystem, Props}

class HeuristicComparison {

  def init(): Unit = {
    val diffVisitor = new AlphaBetaVisitor(new EvaluationByResult)
    val stonesVisitor = new AlphaBetaVisitor(new EvaluatePromoteStones)
    val emptyVisitor = new AlphaBetaVisitor(new EvaluationPromoteEmptyPits)
    val numberOfGame = 5
    val depth = 8
    val toTestVisitor = diffVisitor
    val testData = List( //definition of cases, number of game not used
      new TestData(depth, depth, diffVisitor, diffVisitor, numberOfGame),
      new TestData(depth, depth, stonesVisitor, stonesVisitor, numberOfGame),
      new TestData(depth, depth, emptyVisitor, emptyVisitor, numberOfGame),
      new TestData(depth, depth, diffVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 1, diffVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 2, diffVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 3, diffVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 4, diffVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth, stonesVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 1, stonesVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 2, stonesVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 3, stonesVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 4, stonesVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 1, emptyVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 2, emptyVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 3, emptyVisitor, toTestVisitor, numberOfGame),
      new TestData(depth, depth - 4, emptyVisitor, toTestVisitor, numberOfGame),
    )
    performTest(testData)
  }

  def performTest(testData: List[TestData]): Unit = {
    testData match {
      case Nil => println("end")
      case head :: _ =>
        val callback = new HeuristicsCallback(testData, this)
        println(s"${head.visitorA.evaluationStrategy.getClass.getSimpleName}(${head.aiADepth}) vs " +
          s"${head.visitorB.evaluationStrategy.getClass.getSimpleName}(${head.aiBDepth})")
        ActorSystem().actorOf(Props(classOf[MeasureGameManager], callback, head.visitorA, head.visitorB, head.aiADepth, head.aiBDepth))
    }
  }
}
