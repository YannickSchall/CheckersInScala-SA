package model.gameBoardBaseImpl

import com.google.inject.Inject
import model.PieceInterface

import utils.Mover
import scala.collection.mutable.ListBuffer

abstract class Piece @Inject() (state: String, row: Int, col: Int, color: Color) extends PieceInterface {

  def sList: ListBuffer[String]
  def getColor: Color
  def posToStr(row: Int, col: Int): String = {(col + 65).toChar.toString + (row + 49).toChar.toString}

  def movePossible(to: String, gameBoard: GameBoard): Mover

  def movStrToInt(s: String): (Int, Int, Int, Int) = {
    val startCol = col
    val startRow = row
    val destRow = s.charAt(1).toInt - 49
    val destCol = s.charAt(0).toInt - 65
    (startRow, startCol, destRow, destCol)
  }
}

object Piece {
  def apply(state: String, row: Int, col: Int, color: Color): Piece = state match {
    case "normal" => Normal("normal", row, col, color)
    case "queen" => Queen("queen", row, col, color)
  }


}