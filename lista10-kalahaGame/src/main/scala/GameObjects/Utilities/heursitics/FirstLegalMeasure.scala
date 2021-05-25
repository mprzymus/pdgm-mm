package GameObjects.Utilities.heursitics

import Actors.MeasureVsFirstLegal
import GameObjects.AI.evaluation.{EvaluatePromoteStones, EvaluationByResult, EvaluationPromoteEmptyPits}
import GameObjects.AI.minimax.AlphaBetaVisitor
import GameObjects.Utilities.Callback
import akka.actor.{ActorSystem, Props}

class FirstLegalMeasure {

  def performTest(testData: List[FirstLegalTestData]): Unit = {
    val thisObject = this
    testData match {
      case Nil => println("end")
      case head :: tail =>
        val callback: Callback = new Callback {
          val list: List[FirstLegalTestData] = tail
          val main: FirstLegalMeasure = thisObject
          override def callBack(): Unit = {
            main.performTest(list)
          }
        }
        val str = if (head.firstLegalStarts) s"first legal vs ${head.visitorA.evaluationStrategy.getClass.getSimpleName}"
        else s"${head.visitorA.evaluationStrategy.getClass.getSimpleName} vs firstLegal"
        println(str)
        ActorSystem().actorOf(Props(classOf[MeasureVsFirstLegal], callback, head.visitorA, head.firstLegalStarts, head.aiDepth))
    }
  }

  def init(): Unit = {
    val diffVisitor = new AlphaBetaVisitor(new EvaluationByResult)
    val stonesVisitor = new AlphaBetaVisitor(new EvaluatePromoteStones)
    val emptyVisitor = new AlphaBetaVisitor(new EvaluationPromoteEmptyPits)
    val numberOfGame = 5
    val depth = 11
    val testData = List(
      new FirstLegalTestData(depth, diffVisitor, true, numberOfGame),
      /*new FirstLegalTestData(depth, diffVisitor, false, numberOfGame),
      new FirstLegalTestData(depth, stonesVisitor, true, numberOfGame),
      new FirstLegalTestData(depth, stonesVisitor, false, numberOfGame),
      new FirstLegalTestData(depth, emptyVisitor, true, numberOfGame),
      new FirstLegalTestData(depth, emptyVisitor, false, numberOfGame),*/
    )
    performTest(testData)
  }

}
