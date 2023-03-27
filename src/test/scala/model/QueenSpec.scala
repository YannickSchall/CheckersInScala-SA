package model
import model.gameBoardComponent.gameBoardBaseImpl.{GameBoard, Piece, Queen}
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec._
/*
class QueenSpec extends AnyWordSpec {
  "A Piece called Queen" should {
    val dame = Queen("queen", 4, 4, "white")
    val dameLinks = Queen("queen", 4,0, "white")
    val dameRechts = Queen("queen", 7,9, "white")
    val dameSchwarz = Queen("queen", 2, 2, "black")

    var gb = new GameBoard(10)
    "have a row" in {
      dame.row should be (4)
      dameSchwarz.row should be (2)
    }
    "have a col" in {
      dame.col should be (4)
      dameSchwarz.col should be (2)
    }
    "have a color" in {
      dame.getColor should be ("white")
      dameSchwarz.getColor should be ("black")
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
      dame.whiteMovePossible("A1", gb).getBool should be (true)
      dame.whiteMovePossible("I1", gb).getBool should be (true)
      dame.whiteMovePossible("A9", gb).getBool should be (true)
      dame.whiteMovePossible("J10", gb).getBool should be (true)
      dameLinks.whiteMovePossible("E1", gb).getBool should be (true)
      dameLinks.whiteMovePossible("E9", gb).getBool should be (true)
      dameRechts.whiteMovePossible("H10", gb).getBool should be (true)
      dameRechts.whiteMovePossible("C1", gb).getBool should be (true)
    }
    "should be allowed to Move as a Black Piece" in {
      val dameSchwarzLinks = Queen("queen", 2, 0, "black")
      val dameSchwarzRechts = Queen("queen", 1, 9, "black")
      dameSchwarz.blackMovePossible("A1", gb).getBool should be (true)
      dameSchwarz.blackMovePossible("E1", gb).getBool should be (true)
      dameSchwarz.blackMovePossible("A5", gb).getBool should be (true)
      dameSchwarz.blackMovePossible("H8", gb).getBool should be (true)
      dameSchwarzLinks.blackMovePossible("C1", gb).getBool should be (true)
      dameSchwarzLinks.blackMovePossible("H10", gb).getBool should be (true)
      dameSchwarzRechts.blackMovePossible("B10", gb).getBool should be (true)
      dameSchwarzRechts.blackMovePossible("I1", gb).getBool should be (true)
    }
    "should be allowed to Capture from the middle" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 3, Some(Piece("queen", 3, 3, "black")))
      gbc = gbc.set(3, 5, Some(Piece("queen", 3, 5, "black")))
      gbc = gbc.set(5, 3, Some(Piece("queen", 5, 3, "black")))
      gbc = gbc.set(5, 5, Some(Piece("queen", 5, 5, "black")))
      gbc = gbc.set(4, 4, Some(Piece("queen", 4, 4, "white")))
      var str = ""
      str = gbc.getPiece(4, 4).get.getColor
      val dame2 = gbc.getPiece(4, 4).get //E5
      str should be ("white")
      dame2.whiteMovePossible("C3", gbc).getBool should be (true)
      dame2.whiteMovePossible("G3", gbc).getBool should be (true)
      dame2.whiteMovePossible("C7", gbc).getBool should be (true)
      dame2.whiteMovePossible("G7", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the right" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 7, Some(Piece("queen", 3, 7, "black")))
      gbc = gbc.set(7, 7, Some(Piece("queen", 7, 7, "black")))
      gbc = gbc.set(5, 9, Some(Piece("queen", 5, 9, "white")))
      var str = ""
      str = gbc.getPiece(5, 9).get.getColor
      val dame2 = gbc.getPiece(5, 9).get //E5
      str should be ("white")
      dame2.whiteMovePossible("G9", gbc).getBool should be (true)
      dame2.whiteMovePossible("G3", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the left" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(8, 2, Some(Piece("queen", 8, 2, "black")))
      gbc = gbc.set(5, 1, Some(Piece("queen", 5, 1, "black")))
      gbc = gbc.set(6, 0, Some(Piece("queen", 6, 0, "white")))
      var str = ""
      str = gbc.getPiece(6, 0).get.getColor
      val dame2 = gbc.getPiece(6, 0).get //E5
      str should be ("white")
      dame2.whiteMovePossible("C5", gbc).getBool should be (true)
      dame2.whiteMovePossible("D10", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the middle as Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 3, Some(Piece("queen", 3, 3, "white")))
      gbc = gbc.set(3, 5, Some(Piece("queen", 3, 5, "white")))
      gbc = gbc.set(5, 3, Some(Piece("queen", 5, 3, "white")))
      gbc = gbc.set(5, 5, Some(Piece("queen", 5, 5, "white")))
      gbc = gbc.set(4, 4, Some(Piece("queen", 4, 4, "black")))
      var str = ""
      str = gbc.getPiece(4, 4).get.getColor
      val dame2 = gbc.getPiece(4, 4).get //E5
      str should be ("black")
      dame2.blackMovePossible("C3", gbc).getBool should be (true)
      dame2.blackMovePossible("G3", gbc).getBool should be (true)
      dame2.blackMovePossible("C7", gbc).getBool should be (true)
      dame2.blackMovePossible("G7", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the right as Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 7, Some(Piece("queen", 3, 7, "white")))
      gbc = gbc.set(7, 7, Some(Piece("queen", 7, 7, "white")))
      gbc = gbc.set(5, 9, Some(Piece("queen", 5, 9, "black")))
      var str = ""
      str = gbc.getPiece(5, 9).get.getColor
      val dame2 = gbc.getPiece(5, 9).get //E5
      str should be ("black")
      dame2.blackMovePossible("G9", gbc).getBool should be (true)
      dame2.blackMovePossible("G3", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the left Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(8, 2, Some(Piece("queen", 8, 2, "white")))
      gbc = gbc.set(5, 1, Some(Piece("queen", 5, 1, "white")))
      gbc = gbc.set(6, 0, Some(Piece("queen", 6, 0, "black")))
      var str = ""
      str = gbc.getPiece(6, 0).get.getColor
      val dame2 = gbc.getPiece(6, 0).get //E5
      str should be("black")
      dame2.blackMovePossible("C5", gbc).getBool should be(true)
      dame2.blackMovePossible("D10", gbc).getBool should be(true)
    }
  }
}
*/