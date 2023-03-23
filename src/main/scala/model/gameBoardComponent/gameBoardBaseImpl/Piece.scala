package model.gameBoardComponent.gameBoardBaseImpl

import com.google.inject.Inject
import model.gameBoardComponent.PieceInterface
import util.Mover

import scala.collection.mutable.ListBuffer

abstract class Piece @Inject() (state: String, row: Int, col: Int, color: String) extends PieceInterface {

  def sList: ListBuffer[String]
  def sListBlack: ListBuffer[String]
  def getColor: String
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
  def apply(state: String, row: Int, col: Int, color: String): Piece = state match {
    case "normal" => Normal("normal", row, col, color)
    case "queen" => Queen("queen", row, col, color)
  }


}