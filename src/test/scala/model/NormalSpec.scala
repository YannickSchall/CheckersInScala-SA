package model
import model.gameBoardComponent.gameBoardBaseImpl.{GameBoard, GameBoardCreator, Normal, Piece}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

import scala.collection.mutable.ListBuffer

class NormalSpec extends AnyWordSpec {
  "A Piece called Normal" should {
    val normal = Normal("normal", 4, 4, "white")
    val normalLinks = Normal("normal", 4,0, "white")
    val normalRechts = Normal("normal", 7,9, "white")
    val normalSchwarz = Normal("normal", 2, 2, "black")

    val gb = new GameBoard(10)
    "have a row" in {
      normal.row should be (4)
      normalSchwarz.row should be (2)
    }
    "have a col" in {
      normal.col should be (4)
      normalSchwarz.col should be (2)
    }
    "have a color" in {
      normal.getColor should be ("white")
      normalSchwarz.getColor should be ("black")
    }
    "have the state named Normal" in {
      normal.state should be ("normal")
      normalSchwarz.state should be ("normal")
    }
    "produce a String" in {
      normal.toString should be("\u001B[30mO\u001B[0m")
      normalSchwarz.toString should be ("\u001B[37mO\u001B[0m")
    }

    "be filled into a List" in {
      //normalWhite auf E5 (4,4)
      val size = 10
      var gbc = new GameBoard(size)
      for {index <- 0 until size} {
        for {index2 <- 0 until size} {
          gbc = gbc.set(index, index2, None)
        }
      }
      val n = Normal("normal", 4, 4, "white")
      gbc = gbc.set(5, 5, Some(Piece("normal", 5, 5, "black")))
      gbc = gbc.set(3, 3, Some(Piece("normal", 3, 3, "black")))
      gbc = gbc.set(6, 6, Some(Piece("normal", 6, 6, "white")))
      gbc = gbc.set(4,4, Some(n))
      val list = normal.fillList("C3", gbc, "left", 0)
      val str = gbc.getPiece(4, 4)
      print("my string", str)
      //list should be (ListBuffer(""))
      list.length should be (1)
      //assert(list.length == 2)
      //assert(list.contains("G6 E4"))
      //assert(list.contains("G6 I4"))
      list.head should be ("E5 C3")
      //list(1) should be ("G6 E4")
    }

    "be capturable" in {
      normalLinks.capturable("left", 0, gb) should be (true)
    }

    "should be allowed to Move" in {
      normal.movePossible("D4", gb).getBool should be (true)
      normal.movePossible("F4", gb).getBool should be (true)
      normal.movePossible("A9", gb).getBool should be (false)
      normal.movePossible("J10", gb).getBool should be (false)
      normalLinks.movePossible("B4", gb).getBool should be (true)
      normalLinks.movePossible("E6", gb).getBool should be (false)
      normalRechts.movePossible("I7", gb).getBool should be (true)
      normalRechts.movePossible("I9", gb).getBool should be (false)
    }
    "should be allowed to Move as a Black Piece" in {
      val dameSchwarzLinks = Normal("normal", 2, 0, "black")
      val dameSchwarzRechts = Normal("normal", 1, 9, "black")
      normalSchwarz.movePossible("B4", gb).getBool should be (true)
      normalSchwarz.movePossible("D4", gb).getBool should be (true)
      normalSchwarz.movePossible("A5", gb).getBool should be (false)
      normalSchwarz.movePossible("H8", gb).getBool should be (false)
      dameSchwarzLinks.movePossible("B4", gb).getBool should be (true)
      dameSchwarzLinks.movePossible("B2", gb).getBool should be (false)
      dameSchwarzRechts.movePossible("I3", gb).getBool should be (true)
      dameSchwarzRechts.movePossible("I1", gb).getBool should be (false)
    }
    "should be allowed to Capture from the middle" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 3, Some(Piece("normal", 3, 3, "black")))
      gbc = gbc.set(3, 5, Some(Piece("normal", 3, 5, "black")))
      gbc = gbc.set(5, 3, Some(Piece("normal", 5, 3, "black")))
      gbc = gbc.set(5, 5, Some(Piece("normal", 5, 5, "black")))
      gbc = gbc.set(4, 4, Some(Piece("normal", 4, 4, "white")))
      var str = ""
      str = gbc.getPiece(4, 4).get.getColor
      val normaleWhite = gbc.getPiece(4, 4).get //E5
      str should be ("white")
      normaleWhite.movePossible("C3", gbc).getBool should be (true)
      normaleWhite.movePossible("G3", gbc).getBool should be (true)
      normaleWhite.movePossible("C7", gbc).getBool should be (false)
      normaleWhite.movePossible("G7", gbc).getBool should be (false)
    }
    "should be allowed to Capture from the right" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(4, 8, Some(Piece("normal", 4, 8, "black")))
      gbc = gbc.set(6, 8, Some(Piece("normal", 6, 8, "black")))
      gbc = gbc.set(5, 9, Some(Piece("normal", 5, 9, "white")))
      var str = ""
      str = gbc.getPiece(5, 9).get.getColor
      val normalWhite = gbc.getPiece(5, 9).get //E5
      str should be ("white")
      normalWhite.movePossible("H4", gbc).getBool should be (true)
      normalWhite.movePossible("H8", gbc).getBool should be (false)
    }
    "should be allowed to Capture from the left" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(8, 2, Some(Piece("normal", 8, 2, "black")))
      gbc = gbc.set(5, 1, Some(Piece("normal", 5, 1, "black")))
      gbc = gbc.set(6, 0, Some(Piece("normal", 6, 0, "white")))
      var str = ""
      str = gbc.getPiece(6, 0).get.getColor
      val normalWhite = gbc.getPiece(6, 0).get //E5
      str should be ("white")
      normalWhite.movePossible("C5", gbc).getBool should be (true)
      normalWhite.movePossible("D10", gbc).getBool should be (false)
    }
    "should be allowed to Capture from the middle as Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(3, 3, Some(Piece("normal", 3, 3, "white")))
      gbc = gbc.set(3, 5, Some(Piece("normal", 3, 5, "white")))
      gbc = gbc.set(5, 3, Some(Piece("normal", 5, 3, "white")))
      gbc = gbc.set(5, 5, Some(Piece("normal", 5, 5, "white")))
      gbc = gbc.set(4, 4, Some(Piece("normal", 4, 4, "black")))
      var str = ""
      str = gbc.getPiece(4, 4).get.getColor
      val normaleWhite = gbc.getPiece(4, 4).get //E5
      str should be ("black")
      normaleWhite.movePossible("C3", gbc).getBool should be (false)
      normaleWhite.movePossible("G3", gbc).getBool should be (false)
      normaleWhite.movePossible("C7", gbc).getBool should be (true)
      normaleWhite.movePossible("G7", gbc).getBool should be (true)
    }
    "should be allowed to Capture from the right as Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(4, 8, Some(Piece("normal", 4, 8, "white")))
      gbc = gbc.set(6, 8, Some(Piece("normal", 6, 8, "white")))
      gbc = gbc.set(5, 9, Some(Piece("normal", 5, 9, "black")))
      var str = ""
      str = gbc.getPiece(5, 9).get.getColor
      val dame2 = gbc.getPiece(5, 9).get //E5
      str should be ("black")
      dame2.movePossible("H8", gbc).getBool should be (true)
      dame2.movePossible("H3", gbc).getBool should be (false)
    }
    "should be allowed to Capture from the left Black" in {
      var gbc = new GameBoard(10)
      gbc = gbc.set(7, 1, Some(Piece("normal", 7, 1, "white")))
      gbc = gbc.set(5, 1, Some(Piece("normal", 5, 1, "white")))
      gbc = gbc.set(6, 0, Some(Piece("normal", 6, 0, "black")))
      var str = ""
      str = gbc.getPiece(6, 0).get.getColor
      val dame2 = gbc.getPiece(6, 0).get //E5
      str should be("black")
      dame2.movePossible("C9", gbc).getBool should be(true)
      dame2.movePossible("C5", gbc).getBool should be(false)
    }
  }
}