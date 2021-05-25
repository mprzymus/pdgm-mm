package GameObjects.Utilities.heursitics

import GameObjects.Utilities.{Callback, TestData}

class HeuristicsCallback(list: List[TestData], heuristicComparison: HeuristicComparison) extends Callback {
  override def callBack(): Unit = {
    list match {
      case Nil => ()
      case _ :: tail =>
        heuristicComparison.performTest(tail)
    }
  }
}
