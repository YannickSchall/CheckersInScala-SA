package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import model.gameBoardBaseImpl.GameBoardCreator

class GameBoardCreatorSpec extends AnyWordSpec {
  "A Creator" should {
    val gbc = new GameBoardCreator(8).createBoard()
    "have a size" in {
      gbc.size should be (8)
    } 
    "create an empty board" in {
      val egbc = new GameBoardCreator(10).createEmptyBoard()
      egbc.size should be (10)
    } 
  }
}