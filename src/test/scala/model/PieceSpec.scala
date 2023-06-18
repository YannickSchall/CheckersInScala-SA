package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import model.gameBoardBaseImpl.Color.*
import model.gameBoardBaseImpl.{Normal, Piece, Queen}

class PieceSpec extends AnyWordSpec {
  "A trait" should {
    val norm = Normal("normaL", 0, 0, White)
    val norm2 = Normal("normal", 0, 5, Black)
    "make Integers out of a String " in {
      norm.movStrToInt("B2") should be (0, 0, 1, 1) //C = Ascii 67, "2" =  50
      norm2.movStrToInt("E2") should be (0, 5, 1, 4)
    }
    "apply a Type to a Piece" in {
      val appl = Piece.apply("normal", 0, 1, White)
      val norm3 = Normal("normal", 0, 1,White)
      appl.getColor should be (norm3.getColor)
      appl.state should be(norm3.state)
      appl.row should be(norm3.row)
      appl.col should be(norm3.col)

      val applQ = Piece.apply("queen", 0, 3, Black)
      val queen = Queen("queen", 0, 3, Black)
      applQ.getColor should be (queen.getColor)
      applQ.state should be (queen.state)
      applQ.row should be (queen.row)
      applQ.col should be (queen.col)
    }
    "correctly calculate the position string from integers" in {
      val row4 = 0
      val col4 = 0
      val queen2 = Piece.apply("queen", row4, col4, White)
      val pos = queen2.posToStr(row4, col4)
      pos should be ("A1")
    }
  }
}
