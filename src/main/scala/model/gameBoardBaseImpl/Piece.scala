package model.gameBoardBaseImpl

import com.google.inject.Inject
import model.PieceInterface
import utils.Mover

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

abstract class Piece @Inject() (state: String, row: Int, col: Int, color: Color) extends PieceInterface {

  def sList: ListBuffer[String]
  def getColor: Color
  def posToStr(row: Int, col: Int): String = {(col + 65).toChar.toString + (row + 49).toChar.toString}

  def movePossible(to: String, gameBoard: GameBoard): Future[Mover]

}

object Piece {
  def apply(state: String, row: Int, col: Int, color: Color): Piece = state match {
    case "normal" => Normal("normal", row, col, color)
    case "queen" => Queen("queen", row, col, color)
  }


}