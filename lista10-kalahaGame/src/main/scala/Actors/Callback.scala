package Actors

import GameObjects.Utilities.{MainClass, TestData}

sealed trait Callback {
  def callBack(): Unit
}

class CallbackNextGames(private var games: Int, mainObject: MainClass, testsToDo: List[TestData]) extends Callback {
  override def callBack(): Unit = this.synchronized {
    games -= 1
    if (games <= 0) {
      mainObject.performTest(testsToDo)
    }
  }
}
