package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import gameboard.gameBoardBaseImpl.Color.*
import gameboard.gameBoardBaseImpl.Normal

class PieceSpec extends AnyWordSpec {
  "A trait" should {
    val norm = Normal("normaL", 0, 0, White)
    val norm2 = Normal("normal", 0, 5, Black)
    "make Integers out of a String " in {
      norm.movStrToInt("B2") should be (0, 0, 1, 1) //C = Ascii 67, "2" =  50
      norm2.movStrToInt("E2") should be (0, 5, 1, 4)
    } /*
    "apply a Type to a Piece" in {
      val norm3 = Piece.apply("normal", 0,1, "white") should be (Normal("normal", 0, 1,"white"))
      val queen = Piece.apply("queen", 0,3, "black") should be (Queen("normal", 0, 3,"black"))
    } */
  }
}
