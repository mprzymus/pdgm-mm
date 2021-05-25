package GameObjects.AI.minimax

import GameObjects.Outputs.ConsoleOutput
import GameObjects.Utilities.{Board, PlayerLower, PlayerUpper}
import org.scalatest.funsuite.AnyFunSuite

class FirstLegalVisitorTest extends AnyFunSuite {
  val tested = new FirstLegalVisitor()
  test("first move should be 0") {
    val board = new Board(4)

    assert(tested.minimax(0, PlayerLower(), board) == 0)
    assert(tested.minimax(0, PlayerUpper(), board) == 0)
  }

  test("should return first legal") {
    val board = new Board(0)
    board.toMove = PlayerLower()
    board.pits(4) = 2

    new ConsoleOutput(board).printGame()

    assert(tested.minimax(0, PlayerLower(), board) == 4)
  }
}
