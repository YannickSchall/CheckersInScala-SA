package gameboard

import gameboard.gameBoardBaseImpl.*
import util.*
import com.google.inject.ImplementedBy

import scala.collection.mutable.ListBuffer

@ImplementedBy(classOf[Piece])
trait PieceInterface {
  def sList: ListBuffer[String]
  def sListBlack: ListBuffer[String]
  def state: String
  def row: Int
  def col: Int
  def getColor: Color
  def fillList(to: String, gameBoard: GameBoard, direction: Direction, dist_count: Int): ListBuffer[String]
  def cap_cond(row_offset: Int, col_offset: Int, gameBoard: GameBoard): Boolean
  def capturable(to: String, direction: Direction, row_dist: Int, col_dist: Int, gameBoard: GameBoard): Boolean
  def getMover(to: String, gameBoard: GameBoard): Mover
  def movePossible(to: String, gameBoard: GameBoard): Mover
  def posToStr(row: Int, col: Int): String
  def movStrToInt(s: String): (Int, Int, Int, Int)
  def getDirection(toRow: Int, toCol: Int): Direction

}
