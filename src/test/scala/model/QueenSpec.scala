package model
import model.gameBoardBaseImpl.Color.*
import model.gameBoardBaseImpl.{GameBoard, GameBoardCreator, Piece, Queen}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

class QueenSpec extends AnyWordSpec {
  "A Piece called Queen" should {
    val dame = Queen("queen", 4, 4, White)
    val dameLinks = Queen("queen", 4,0, White)
    val dameRechts = Queen("queen", 7,9, White)
    val dameSchwarz = Queen("queen", 2, 2, Black)

    val gb = new GameBoardCreator(10).createEmptyBoard()
    "have a row" in {
      dame.row should be (4)
      dameSchwarz.row should be (2)
    }
    "have a col" in {
      dame.col should be (4)
      dameSchwarz.col should be (2)
    }
    "have a color" in {
      dame.getColor should be (White)
      dameSchwarz.getColor should be (Black)
    }
    "have the state named queen" in {
      dame.state should be ("queen")
      dameSchwarz.state should be ("queen")
    }
    "produce a String" in {
      dame.toString should be("\u001B[30mQ\u001B[0m")
      dameSchwarz.toString should be ("\u001B[37mQ\u001B[0m")
    }
    "should be allowed to Move" in {
      dame.movePossible("A1", gb).getBool should be (true)
      dame.movePossible("I1", gb).getBool should be (true)
      dame.movePossible("A9", gb).getBool should be (true)
      dame.movePossible("J10", gb).getBool should be (true)
      dameLinks.movePossible("E1", gb).getBool should be (true)
      dameLinks.movePossible("E9", gb).getBool should be (true)
      dameRechts.movePossible("H10", gb).getBool should be (true)
      dameRechts.movePossible("C1", gb).getBool should be (true)
    }
    "should be allowed to Move as a Black Piece" in {
      val gb = new GameBoardCreator(10).createEmptyBoard()
      val dameSchwarzLinks = Queen("queen", 2, 0, Black)
      val dameSchwarzRechts = Queen("queen", 1, 9, Black)
      val dameSchwarz = Queen("queen", 2, 2, Black)
      dameSchwarz.movePossible("A1", gb).getBool should be (true)
      //0 should be (1)
      dameSchwarz.movePossible("E1", gb).getBool should be (true)
      //1 should be (2)
      dameSchwarz.movePossible("A5", gb).getBool should be (true)
      //1 should be (3)
      dameSchwarz.movePossible("H8", gb).getBool should be (true)
      //1 should be (4)
      dameSchwarzLinks.movePossible("C1", gb).getBool should be (true)
      //1 should be (5)
      dameSchwarzLinks.movePossible("H10", gb).getBool should be (true)
      dameSchwarzRechts.movePossible("B10", gb).getBool should be (true)
      dameSchwarzRechts.movePossible("I1", gb).getBool should be (true)
    }
    "should be allowed to Capture from the middle" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 3, Some(Piece("queen", 3, 3, Black)))
      gbc = gbc.set(3, 5, Some(Piece("queen", 3, 5, Black)))
      gbc = gbc.set(5, 3, Some(Piece("queen", 5, 3, Black)))
      gbc = gbc.set(5, 5, Some(Piece("queen", 5, 5, Black)))
      gbc = gbc.set(4, 4, Some(Piece("queen", 4, 4, White)))
      val color = gbc.getPiece(4, 4).get.getColor
      val dame2 = gbc.getPiece(4, 4).get //E5
      color should be (White)
      dame2.movePossible("C3", gbc).getBool should be (true)
      dame2.movePossible("G3", gbc).getBool should be (true)
      dame2.movePossible("C7", gbc).getBool should be (true)
      dame2.movePossible("G7", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the right" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 7, Some(Piece("queen", 3, 7, Black)))
      gbc = gbc.set(7, 7, Some(Piece("queen", 7, 7, Black)))
      gbc = gbc.set(5, 9, Some(Piece("queen", 5, 9, White)))
      val color = gbc.getPiece(5, 9).get.getColor
      val dame2 = gbc.getPiece(5, 9).get //E5
      color should be (White)
      dame2.movePossible("G9", gbc).getBool should be (true)
      dame2.movePossible("G3", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the left" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(8, 2, Some(Piece("queen", 8, 2, Black)))
      gbc = gbc.set(5, 1, Some(Piece("queen", 5, 1, Black)))
      gbc = gbc.set(6, 0, Some(Piece("queen", 6, 0, White)))
      val color = gbc.getPiece(6, 0).get.getColor
      val dame2 = gbc.getPiece(6, 0).get //E5
      color should be (White)
      dame2.movePossible("C5", gbc).getBool should be (true)
      dame2.movePossible("D10", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the middle as Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 3, Some(Piece("queen", 3, 3, White)))
      gbc = gbc.set(3, 5, Some(Piece("queen", 3, 5, White)))
      gbc = gbc.set(5, 3, Some(Piece("queen", 5, 3, White)))
      gbc = gbc.set(5, 5, Some(Piece("queen", 5, 5, White)))
      gbc = gbc.set(4, 4, Some(Piece("queen", 4, 4, Black)))
      val color = gbc.getPiece(4, 4).get.getColor
      val dame2 = gbc.getPiece(4, 4).get //E5
      color should be (Black)
      dame2.movePossible("C3", gbc).getBool should be (true)
      dame2.movePossible("G3", gbc).getBool should be (true)
      dame2.movePossible("C7", gbc).getBool should be (true)
      dame2.movePossible("G7", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the right as Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 7, Some(Piece("queen", 3, 7, White)))
      gbc = gbc.set(7, 7, Some(Piece("queen", 7, 7, White)))
      gbc = gbc.set(5, 9, Some(Piece("queen", 5, 9, Black)))
      val color = gbc.getPiece(5, 9).get.getColor
      val dame2 = gbc.getPiece(5, 9).get //E5
      color should be (Black)
      dame2.movePossible("G9", gbc).getBool should be (true)
      dame2.movePossible("G3", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the left Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(8, 2, Some(Piece("queen", 8, 2, White)))
      gbc = gbc.set(5, 1, Some(Piece("queen", 5, 1, White)))
      gbc = gbc.set(6, 0, Some(Piece("queen", 6, 0, Black)))
      val color = gbc.getPiece(6, 0).get.getColor
      val dame2 = gbc.getPiece(6, 0).get //E5
      color should be (Black)
      dame2.movePossible("C5", gbc).getBool should be(true)
      dame2.movePossible("D10", gbc).getBool should be(true)
    }
    "should not be allowed to Capture their own color" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(8, 2, Some(Piece("queen", 8, 2, White)))
      gbc = gbc.set(5, 1, Some(Piece("queen", 5, 1, White)))
      gbc = gbc.set(6, 0, Some(Piece("queen", 6, 0, White)))
      val dame2 = gbc.getPiece(6, 0).get //E5
      dame2.movePossible("C5", gbc).getBool should be(false)
      dame2.movePossible("D10", gbc).getBool should be(false)
    }
  }
}
