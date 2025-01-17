package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import model.gameBoardBaseImpl.*
import model.gameBoardBaseImpl.Color.*
import model.gameBoardBaseImpl.Direction.*

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class NormalSpec extends AnyWordSpec {
  "A Piece called Normal" should {

    // black right no cap
    val size = 10
    var game = new GameBoardCreator(size).createEmptyBoard()

    // define white stones
    val w1 = Normal("normal", 3, 3, White)
    val w2 = Normal("normal", 3, 7, White)
    val w3 = Normal("normal", 4, 2, White)
    val w4 = Normal("normal", 4, 4, White)
    val w5 = Normal("normal", 8, 8, White)

    // define black stones
    val b1 = Normal("normal", 1, 1, Black)
    val b2 = Normal("normal", 3, 1, Black)
    val b3 = Normal("normal", 3, 5, Black)
    val b4 = Normal("normal", 4, 6, Black)
    val b5 = Normal("normal", 9, 9, Black)

    // preset white stones
    game.set(3, 3, Some(w1)) //D4
    game.set(3, 7, Some(w2)) //H4
    game.set(4, 2, Some(w3)) //C5
    game.set(4, 4, Some(w4)) //E5
    game.set(8, 8, Some(w5)) //I9

    // preset black stones
    game.set(1, 1, Some(b1)) //B2
    game.set(3, 1, Some(b2)) //B4
    game.set(3, 5, Some(b3)) //F4
    game.set(4, 6, Some(b4)) //G5
    game.set(9, 9, Some(b5)) //J10

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
      w1.getColor should be (White)
      b1.getColor should be (Black)
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

      var listW = new ListBuffer[String]
      var listB = new ListBuffer[String]
      // cap over LEFT true RIGHT nok cause w over w
      listW = w3.fillList("A3", game, Left, 0)
      listW = w3.fillList("E3", game, Right, 0)
      //b5.fillList("H8", game, Left, 0)
      // cap over RIGHT true
      //listW = w4.fillList("G3", game, Right, 0)
      // cap over RIGHT true LEFT nok cause b o
      listB = b4.fillList("I7", game, Right, 0)
      listB = b4.fillList("E3", game, Left, 0)

      listW.length should be (2)
      listB.length should be (2)
      print(listW)
      print(listB)

      listW.head should be ("C5 A3")
      listW(1) should be ("C5 E3")
      listB.head should be ("G5 I7")
      listB(1) should be ("G5 E7")

    }

    "should be allowed to Capture from the middle" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 3, Some(Piece("normal", 3, 3, Black)))
      gbc = gbc.set(3, 5, Some(Piece("normal", 3, 5, Black)))
      gbc = gbc.set(5, 3, Some(Piece("normal", 5, 3, Black)))
      gbc = gbc.set(5, 5, Some(Piece("normal", 5, 5, Black)))
      gbc = gbc.set(4, 4, Some(Piece("normal", 4, 4, White)))
      val color = gbc.getPiece(4, 4).get.getColor
      val normaleWhite = gbc.getPiece(4, 4).get //E5
      color should be (White)
      Await.result(normaleWhite.movePossible("C3", gbc), 5.seconds).getBool should be (true)
      Await.result(normaleWhite.movePossible("G3", gbc), 5.seconds).getBool should be (true)
      Await.result(normaleWhite.movePossible("C7", gbc), 5.seconds).getBool should be (false)
      Await.result(normaleWhite.movePossible("G7", gbc), 5.seconds).getBool should be (false)
    }
    "should be allowed to Capture from the right" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(4, 8, Some(Piece("normal", 4, 8, Black)))
      gbc = gbc.set(6, 8, Some(Piece("normal", 6, 8, Black)))
      gbc = gbc.set(5, 9, Some(Piece("normal", 5, 9, White)))
      val color = gbc.getPiece(5, 9).get.getColor
      val normalWhite = gbc.getPiece(5, 9).get //E5
      color should be (White)
      Await.result(normalWhite.movePossible("H4", gbc), 5.seconds).getBool should be (true)
      Await.result(normalWhite.movePossible("H8", gbc), 5.seconds).getBool should be (false)
    }
    "should be allowed to Capture from the left" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(8, 2, Some(Piece("normal", 8, 2, Black)))
      gbc = gbc.set(5, 1, Some(Piece("normal", 5, 1, Black)))
      gbc = gbc.set(6, 0, Some(Piece("normal", 6, 0, White)))
      val color = gbc.getPiece(6, 0).get.getColor
      val normalWhite = gbc.getPiece(6, 0).get //E5
      color should be (White)
      Await.result(normalWhite.movePossible("C5", gbc), 5.seconds).getBool should be (true)
      Await.result(normalWhite.movePossible("D10", gbc), 5.seconds).getBool should be (false)
    }
    "should be allowed to Capture from the middle as Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(3, 3, Some(Piece("normal", 3, 3, White)))
      gbc = gbc.set(3, 5, Some(Piece("normal", 3, 5, White)))
      gbc = gbc.set(5, 3, Some(Piece("normal", 5, 3, White)))
      gbc = gbc.set(5, 5, Some(Piece("normal", 5, 5, White)))
      gbc = gbc.set(4, 4, Some(Piece("normal", 4, 4, Black)))
      val color = gbc.getPiece(4, 4).get.getColor
      val normaleWhite = gbc.getPiece(4, 4).get //E5
      color should be (Black)
      Await.result(normaleWhite.movePossible("C3", gbc), 5.seconds).getBool should be (false)
      Await.result(normaleWhite.movePossible("G3", gbc), 5.seconds).getBool should be (false)
      Await.result(normaleWhite.movePossible("C7", gbc), 5.seconds).getBool should be (true)
      Await.result(normaleWhite.movePossible("G7", gbc), 5.seconds).getBool should be (true)
    }
    "should be allowed to Capture from the right as Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(4, 8, Some(Piece("normal", 4, 8, White)))
      gbc = gbc.set(6, 8, Some(Piece("normal", 6, 8, White)))
      gbc = gbc.set(5, 9, Some(Piece("normal", 5, 9, Black)))
      val color = gbc.getPiece(5, 9).get.getColor
      val black = gbc.getPiece(5, 9).get //E5
      color should be (Black)
      Await.result(black.movePossible("H8", gbc), 5.seconds).getBool should be (true)
      Await.result(black.movePossible("H3", gbc), 5.seconds).getBool should be (false)
    }
    "should be allowed to Capture from the left as Black" in {
      var gbc = new GameBoardCreator(10).createEmptyBoard()
      gbc = gbc.set(4, 1, Some(Piece("normal", 4, 1, White)))
      gbc = gbc.set(6, 1, Some(Piece("normal", 6, 1, White)))
      gbc = gbc.set(5, 0, Some(Piece("normal", 5, 0, Black)))
      val color = gbc.getPiece(5, 0).get.getColor
      val normalBlack = gbc.getPiece(5, 0).get //E5
      color should be (Black)
      Await.result(normalBlack.movePossible("C8", gbc), 5.seconds).getBool should be (true)
      Await.result(normalBlack.movePossible("D1", gbc), 5.seconds).getBool should be (false)
    }
    "should be allowed to move but not capture" in {
      Await.result(w5.movePossible("H8", game), 5.seconds).getBool should be(true)
      Await.result(b4.movePossible("H6", game), 5.seconds).getBool should be(false)
      //b4 4,6 -> 5,7
    }
    "should not be allowed to move and not capture" in {
      Await.result(w5.movePossible("H9", game), 5.seconds).getBool should be(false)
    }
    "get a direction" in {
      val n = Normal("normal", 0, 0, Black)
      n.getDirection(1, 1) should be (Right)
    }
    "not be allowed to capture with invalid direction" in {
      val n = Normal("normal", 0, 0, Black)
      val gb = new GameBoard(10)
      n.capturable("B2", DownRight, 1, 1, gb) should be (false)
    }
  }

}