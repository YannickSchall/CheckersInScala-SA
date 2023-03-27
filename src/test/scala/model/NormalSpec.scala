package model
import model.gameBoardComponent.gameBoardBaseImpl.{GameBoard, GameBoardCreator, Normal, Piece}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

import scala.collection.mutable.ListBuffer

class NormalSpec extends AnyWordSpec {
  "A Piece called Normal" should {
    /*
    val gb = new GameBoard(10)
    val normal = Normal("normal", 4, 4, "white")
    val normalLinks = Normal("normal", 4,0, "white")
    val normalRechts = Normal("normal", 7,9, "white")
    val normalSchwarz = Normal("normal", 2, 2, "black")
    */

    val size = 10
    var game = new GameBoard(size)

    // define white stones
    val w1 = Normal("normal", 3, 3, "white")
    val w2 = Normal("normal", 3, 7, "white")
    val w3 = Normal("normal", 4, 2, "white")
    val w4 = Normal("normal", 4, 4, "white")
    val w5 = Normal("normal", 8, 8, "white")

    // define black stones
    val b1 = Normal("normal", 1, 1, "black")
    val b2 = Normal("normal", 3, 1, "black")
    val b3 = Normal("normal", 3, 5, "black")
    val b4 = Normal("normal", 4, 6, "black")
    val b5 = Normal("normal", 9, 9, "black")

    // preset white stones
    game.set(3, 3, Some(w1)) //D4
    game.set(3, 7, Some(w2)) //H4
    game.set(4, 2, Some(w3)) //C5
    game.set(4, 4, Some(w4)) //E5
    game.set(8, 8, Some(w5)) //J9

    // preset black stones
    game.set(1, 1, Some(b1)) //B2
    game.set(3, 1, Some(b2)) //B4
    game.set(3, 5, Some(b3)) //F4
    game.set(4, 6, Some(b4)) //G5
    game.set(9, 9, Some(b5)) //I10

    // fill board
    for {index <- 0 until size} {
      for {index2 <- 0 until size} {
        game = game.set(index, index2, None)
      }
    }

    "have a row" in {
      w1.row should be (3)
      b1.row should be (1)
    }
    "have a col" in {
      w1.col should be (3)
      b1.col should be (1)
    }
    "have a color" in {
      w1.getColor should be ("white")
      b1.getColor should be ("black")
    }
    "have the state named Normal" in {
      w1.state should be ("normal")
      b1.state should be ("normal")
    }
    "produce a String" in {
      w1.toString should be("\u001B[30mO\u001B[0m")   // black o for tui
      b1.toString should be ("\u001B[37mO\u001B[0m") // white o for tui
    }

    "be filled into a List" in {
      /*
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
      gbc = gbc.set(0,1, Some(Piece("normal", 0, 1, "black")))

      val str = gbc.getPiece(4, 4)
      val str1 = gbc.getPiece(0,1).toString
      */

      var listW = new ListBuffer[String]
      var listB = new ListBuffer[String]
      // cap over LEFT true RIGHT nok cause w over w
      listW = w3.fillList("A3", game, "left", 0)
      listW = w3.fillList("E3", game, "right", 0)
      //b5.fillList("H8", game, "left", 0)
      // cap over RIGHT true
      //listW = w4.fillList("G3", game, "right", 0)
      // cap over RIGHT true LEFT nok cause b o
      listB = b4.fillList("I7", game, "right", 0)
      listB = b4.fillList("E3", game, "right", 0)

      listW.length should be (2)
      listB.length should be (2)
      print(listW)
      print(listB)

      //listW.head should be ("C5 A3")
      //listB.head should be ("G5 I3")

    }

    "be capturable" in {
      //print(gb.getPiece(7, 9)) // white
      //print(gb.getPiece(2, 2)) // black
      /*
      normalLinks.capturable("left", 0, 0, gb) should be (false)
      normalRechts.capturable("right", 0, 0, gb) should be (false) // wenn rechts am rand
      */
    }

    "should be allowed to Move" in {
      // normal ist (4,4) white E5
      /*
      normal.movePossible("D4", gb).getBool should be (true) //move top left to empty space
      normal.movePossible("F4", gb).getBool should be (true)
      normal.movePossible("A9", gb).getBool should be (false)
      normal.movePossible("J10", gb).getBool should be (false)
      normalLinks.movePossible("B4", gb).getBool should be (true)
      normalLinks.movePossible("E6", gb).getBool should be (false)
      normalRechts.movePossible("I7", gb).getBool should be (true)
      normalRechts.movePossible("I9", gb).getBool should be (false)
      */
    }
    "should be allowed to Move as a Black Piece" in {
      /*
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
      */
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