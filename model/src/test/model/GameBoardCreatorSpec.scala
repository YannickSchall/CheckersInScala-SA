package model
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import gameboard.gameBoardBaseImpl.GameBoardCreator

class GameBoardCreatorSpec extends AnyWordSpec {
  "A Creator" should {
    val gbc = new GameBoardCreator(8).createBoard()
    "have a size" in {
      gbc.size should be (8)
    } /*
    "have white corners at " in {
      gbc.field(0,0).piece.toString should be ("Some(O)")
    } */
  }
}