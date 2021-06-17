package model
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec._
class GameBoardSpec extends AnyWordSpec {
  "A GameBoard" should {
    val board = new GameBoard(3)
    val boardGross = new GameBoard(8)
    val player = new Player("Simon", 0, "white")
    val modus = Classic()
    "have fields" in {
      board.size should be (3)
    }
    "Convert The String of a Field to a col-number" in {
      board.colToInt("A3") should be (2)
    }
    "Convert The String of a Field to a row-number" in {
      board.rowToInt("C2") should be (2)
    }
    "Convert Position to a String" in {
      board.posToStr(1,1) should be ("B2")
    }
    "Show a PlayField" in {
      board.toString should be ("\n+-------+\n|       |\n|       |\n|       |\n+-------+\n")
    }
  }
}