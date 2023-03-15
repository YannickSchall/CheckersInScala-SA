package controller.controllerComponent
import controller.controllerComponent.GameState.GameState
import model.gameBoardComponent.{FieldInterface, PieceInterface}
import model.gameBoardComponent.gameBoardBaseImpl.Piece
import util.Mover
import scala.swing.Publisher
import scala.swing.event.Event
import scala.compiletime.erasedValue

trait ControllerInterface extends Publisher {

  def gameState: GameState
  var cap: String
  var destTemp: String
  def createNewGameBoard(): Unit
  def resize(newSize: Int): Unit
  def createGameBoard(size: Int): Unit
  def setGameState(newGameState: GameState): Unit
  def gameBoardToString: String
  def getPiece(row: Int, col: Int): Option[PieceInterface]
  def set(row: Int, col: Int, piece: Piece): Unit
  def remove(row: Int, col: Int): Unit
  def move(start: String, dest: String): Unit
  def movePossible(start: String, dest: String): Mover
  def save: Unit
  def load: Unit
  def undo: Unit
  def redo: Unit
  def isSet(row: Int, col: Int): Boolean
  def field(row: Int, col: Int): FieldInterface
  def gameBoardSize: Int
  def statusText: String
}

class FieldChanged extends Event
case class GBSizeChanged(newSize: Int) extends Event