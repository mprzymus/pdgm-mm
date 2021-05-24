package GameObjects.AI.minimax

import GameObjects.AI.evaluation.EvaluationByResult
import GameObjects.Outputs.ConsoleOutput
import GameObjects.Utilities.{Board, GameFinished, PlayerLower, PlayerUpper}
import org.scalatest.funsuite.AnyFunSuite

class AlphaBetaVisitorTest extends AnyFunSuite {
  val miniMaxVisitor: MiniMaxVisitor = new AlphaBetaVisitor(new EvaluationByResult)
  val miniMaxAlgorithm = new MinMaxAlgorithm(new Board(1), PlayerLower(), new FixedDepth(3))


  test("should take test") {
    val board = new Board(0)
    board.toMove = PlayerLower()
    val pits = board.pits
    for (i <- 0 to 2)
      pits(i * 2) = 1
    for (i <- 0 to 1)
      pits(i * 2 + 9) = 100 / (i * 10 + 1)


    val resultForWinner = miniMaxVisitor.minimax(5, PlayerLower(), board)
    new ConsoleOutput(board).printGame()
    board.toMove = PlayerUpper()
    val resultForLoser = miniMaxVisitor.minimax(5, PlayerUpper(), board)

    assert(resultForWinner == 2)
    assert(resultForLoser == 2)
  }

  test("redo move test") {
    val board = new Board(0)
    board.toMove = PlayerLower()
    val pits = board.pits
    pits(5) = 1
    pits(4) = 2
    pits(3) = 4
    pits(10) = 1000
    new ConsoleOutput(board).printGame()

    val result = miniMaxVisitor.minimax(3, PlayerLower(), board)

    assert(result == 5)
  }

  test("game test") {
    val board = new Board(4)
    val depth = 4
    val miniMaxVisitorToCompare = new MiniMaxVisitor(new EvaluationByResult())
    board.toMove = PlayerLower()
    val output = new ConsoleOutput(board)

    while (board.toMove != GameFinished()) {
      output.printGame()
      val result = miniMaxVisitor.minimax(depth, board.toMove, board)
      val controlled = miniMaxVisitorToCompare.minimax(depth, board.toMove, board)
      assert(result == controlled)
      board.move(result, board.toMove)
    }
  }
}
