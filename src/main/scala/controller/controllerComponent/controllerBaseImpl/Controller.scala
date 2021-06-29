package controller.controllerComponent.controllerBaseImpl
import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import controller.controllerComponent.GameState._
import controller.controllerComponent.{ControllerInterface, FieldChanged, GBSizeChanged, GameState}
import model.fileIoComponent.FileIOInterface
import model.gameBoardComponent.{FieldInterface, GameBoardInterface, PieceInterface}
import model.gameBoardComponent.gameBoardBaseImpl.{Field, GameBoard, GameBoardCreator, Piece}
import util.{Mover, UndoManager}

import scala.swing.Publisher

class Controller @Inject() (var gameBoard: GameBoardInterface) extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager
  var gameState: GameState = WHITE_TURN
  val injector = Guice.createInjector(new CheckersModule)
  val fileIo = injector.instance[FileIOInterface]

  
  def createNewGameBoard(): Unit = {
    gameBoard.size match {
      case 8 => gameBoard = injector.instance[GameBoardInterface](Names.named("8")); gameState = WHITE_TURN
      case 10 => gameBoard = injector.instance[GameBoardInterface](Names.named("10")); gameState = WHITE_TURN
      case _ =>
    }
    publish(GBSizeChanged(gameBoard.size))
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def resize(newSize: Int): Unit = {
    newSize match {
      case 8 => gameBoard = injector.instance[GameBoardInterface](Names.named("8")); gameBoard = new GameBoardCreator(newSize).createBoard(); gameState = WHITE_TURN
      case 10 => gameBoard = injector.instance[GameBoardInterface](Names.named("10")); gameBoard = new GameBoardCreator(newSize).createBoard(); gameState = WHITE_TURN
      case _ =>
    }
    publish(GBSizeChanged(newSize))
    publish(new FieldChanged)
    publish(new PrintTui)
    newSize
  }

  def createGameBoard(size: Int): Unit = {
    gameBoard = new GameBoardCreator(size).createBoard()
    gameState = WHITE_TURN
    publish(GBSizeChanged(gameBoard.size))
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def setGameState(newGameState: GameState): Unit = {
    gameState = newGameState
  }

  def gameBoardToString: String = gameBoard.toString

  def getPiece(row: Int, col: Int): Option[PieceInterface] = {
    gameBoard.getPiece(row, col)
  }

  def set(row: Int, col: Int, piece: Piece): Unit = {
    undoManager.doStep(new SetCommand(row, col, Some(piece), this))
    //gameBoard = gameBoard.set(row, col, piece)
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def remove(row: Int, col: Int): Unit = {
    undoManager.doStep(new SetCommand(row, col, None, this))
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def move(start: String, dest: String): Unit = {
    if (gameState == WHITE_TURN && gameBoard.getField(start).getPiece.get.getColor == "white") {
      if (this.movePossible(start, dest).getRem.isBlank) gameState = BLACK_TURN
      undoManager.doStep(new MoveCommand(start, dest, this))
      //gameBoard = gameBoard.move(start, dest)
      publish(new FieldChanged)
      publish(new PrintTui)

    } else if (gameState == BLACK_TURN && gameBoard.getField(start).getPiece.get.getColor == "black") {
      if (this.movePossible(start, dest).getRem.isBlank) gameState = WHITE_TURN
      undoManager.doStep(new MoveCommand(start, dest, this))
      //gameBoard = gameBoard.move(start, dest)
      publish(new FieldChanged)
      publish(new PrintTui)
    }
    /*else if (gameState == WHITE_CAP && gameBoard.getField(start).getPiece.get.getColor == "white" && ) {
      if (this.movePossible(start, dest).getRem.isBlank) {
        gameState = BLACK_TURN
      }
      undoManager.doStep(new MoveCommand(start, dest, this))
      //gameBoard = gameBoard.move(start, dest)
      publish(new FieldChanged)
      publish(new PrintTui)
    */}



  def movePossible(start: String, dest: String): Mover = {
    if (gameBoard.getField(start).piece.isDefined) {
      if (gameBoard.getField(start).piece.get.getColor == "black") gameBoard.blackMovePossible(start, dest)
      else gameBoard.whiteMovePossible(start, dest)
    } else new Mover(false, "", false)
  }

  def save: Unit = {
    fileIo.save(gameBoard)
    publish(new FieldChanged)
  }

  def load: Unit = {
    val oldSize = gameBoard.size
    gameBoard = fileIo.load
    if (gameBoard.size != oldSize) publish(new GBSizeChanged(gameBoard.size))
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def undo: Unit = {
    undoManager.undoStep
    //print(undoManager.undoStack)
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def redo: Unit = {
    undoManager.redoStep
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def isSet(row: Int, col: Int): Boolean = gameBoard.field(row, col).isSet

  def field(row: Int, col: Int):FieldInterface = gameBoard.field(row,col)

  def gameBoardSize: Int = gameBoard.size

  def statusText: String = GameState.message(gameState)
  /*
  def highlight(index: Int): Unit = {
    gameBoard = gameBoard.highlight(index)
    publish(new FieldChanged)
  }
  */
}