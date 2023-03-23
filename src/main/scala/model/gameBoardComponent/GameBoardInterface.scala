package model.gameBoardComponent
import model.gameBoardComponent.gameBoardBaseImpl._
import util.Mover

trait GameBoardInterface {
  def size: Int
  def getField(pos: String): FieldInterface
  def remove(row: Int, col: Int): GameBoardInterface
  def set(row: Int, col: Int, piece: Option[Piece]): GameBoardInterface
  def field(row: Int, col: Int): FieldInterface
  def colToInt(pos: String): Int
  def rowToInt(pos: String): Int
  def posToStr(row: Int, col: Int): String
  def toString: String
  def getPiece(row: Int, col: Int): Option[PieceInterface]
  def move(start: String, dest: String): GameBoardInterface
  def movePossible(start: String, dest: String): Mover
}

trait FieldInterface {
  def isSet: Boolean
  def getPos: String
  def getPiece: Option[Piece]
  def toString: String
  def piece: Option[Piece]
}