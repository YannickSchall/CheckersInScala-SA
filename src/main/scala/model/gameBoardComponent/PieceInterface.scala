package model.gameBoardComponent
import com.google.inject.ImplementedBy
import model.gameBoardComponent.gameBoardBaseImpl.{GameBoard, Piece}
import util.Mover

import scala.collection.mutable.ListBuffer

@ImplementedBy(classOf[Piece])
trait PieceInterface {
  def sList: ListBuffer[String]
  def sListBlack: ListBuffer[String]
  def state: String
  def row: Int
  def col: Int

  def getColor: String

  def fillList(to: String, gameBoard: GameBoard, x: Int): ListBuffer[String]
  def capturable(to: String, dist: Int, gameBoard: GameBoard): Boolean
  def getMover(to: String, gameBoard: GameBoard): Mover
  def movePossible(to: String, gameBoard: GameBoard): Mover

  def posToStr(row: Int, col: Int): String
  def movStrToInt(s: String): (Int, Int, Int, Int)

}
