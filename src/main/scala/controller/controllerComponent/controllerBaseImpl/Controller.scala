package controller.controllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions.*
import controller.controllerComponent.GameState.*
import gameboard.gameBoardBaseImpl.Color.*
import controller.controllerComponent.{ControllerInterface, FieldChanged, GBSizeChanged, GameState}
import gameboard.{FieldInterface, GameBoardInterface, PieceInterface}
import utils.*
import gameboard.gameBoardBaseImpl.*
import scala.concurrent.Future

import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode, HttpMethods, HttpResponse, HttpRequest}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.Checkers.{controller, gui}
import scala.util.{Failure, Success, Try}
import scala.swing.Publisher

class Controller @Inject() (var gameBoard: GameBoardInterface) extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager
  var gameState: GameState = WHITE_TURN
  val injector = Guice.createInjector(new CheckersModule)
  //val fileIo = injector.instance[FileIOInterface]
  var cap: String = ""
  var destTemp: String = ""
  val fileIOServer = "http://localhost:8081/fileio"

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
    //newSize
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
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def remove(row: Int, col: Int): Unit = {
    undoManager.doStep(new SetCommand(row, col, None, this))
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def move(start: String, dest: String): Unit = {

    if (gameState == WHITE_TURN && gameBoard.getField(start).getPiece.get.getColor == White) {
      cap = ""
      gameBoard.getField(start).getPiece.get.sListBlack.clear
      gameBoard.getField(start).getPiece.get.sList.clear
      // case einbauen
      if (this.movePossible(start, dest).getRem.isEmpty) gameState = BLACK_TURN
      if (!this.movePossible(start, dest).getRem.isEmpty) cap = this.movePossible(start, dest).getRem
      undoManager.doStep(new MoveCommand(start, dest, this))
      if (!cap.isEmpty) {
        gameBoard.getField(dest).getPiece.get.sList.clear
        gameBoard.getField(dest).getPiece.get.sListBlack.clear
        this.gameBoard = gameBoard.remove(gameBoard.rowToInt(cap), gameBoard.colToInt(cap))
        this.movePossible(dest, dest)
        cap = ""
        if (gameBoard.getField(dest).getPiece.get.sList.nonEmpty) {
          gameState = WHITE_CAP
          destTemp = dest
        } else gameState = BLACK_TURN
      } else gameState = BLACK_TURN
      cap = ""
      publish(new FieldChanged)
      publish(new PrintTui)
      return
    }

    if (gameState == BLACK_TURN && gameBoard.getField(start).getPiece.get.getColor == Black) {
      cap = ""
      gameBoard.getField(start).getPiece.get.sListBlack.clear
      gameBoard.getField(start).getPiece.get.sList.clear
      if (this.movePossible(start, dest).getRem.isEmpty) gameState = WHITE_TURN
      if (!this.movePossible(start, dest).getRem.isEmpty) cap = this.movePossible(start, dest).getRem
      undoManager.doStep(new MoveCommand(start, dest, this))
      if (!cap.isEmpty) {
        gameBoard.getField(dest).getPiece.get.sListBlack.clear
        gameBoard.getField(dest).getPiece.get.sList.clear
        this.gameBoard = gameBoard.remove(gameBoard.rowToInt(cap), gameBoard.colToInt(cap))
        this.movePossible(dest, dest)
        cap = ""
        if (gameBoard.getField(dest).getPiece.get.sListBlack.nonEmpty) {
          gameState = BLACK_CAP
          destTemp = dest
        } else gameState = WHITE_TURN
      } else gameState = WHITE_TURN
      cap = ""
      publish(new FieldChanged)
      publish(new PrintTui)
      return
    }

    else if (gameState == WHITE_CAP && start == destTemp) {
      if (!this.movePossible(start, dest).getRem.isEmpty) cap = this.movePossible(start, dest).getRem
      gameBoard.getField(start).getPiece.get.sList.clear
      gameBoard.getField(start).getPiece.get.sListBlack.clear
      this.movePossible(start, start)
      if (gameBoard.getField(start).getPiece.get.sList.nonEmpty) {
        if (!this.movePossible(start, dest).getRem.isEmpty) {
          undoManager.doStep(new MoveCommand(start, dest, this))
          gameBoard.getField(dest).getPiece.get.sList.clear
          gameBoard.getField(dest).getPiece.get.sListBlack.clear
          this.gameBoard = gameBoard.remove(gameBoard.rowToInt(cap), gameBoard.colToInt(cap))
          this.movePossible(dest, dest)
          if (gameBoard.getField(dest).getPiece.get.sList.isEmpty) gameState = BLACK_TURN
          destTemp = dest
          publish(new FieldChanged)
          publish(new PrintTui)
          cap = ""
        }
      } else gameState = BLACK_TURN
      return
    }

    else if (gameState == BLACK_CAP && start == destTemp) {
      if (!this.movePossible(start, dest).getRem.isEmpty) cap = this.movePossible(start, dest).getRem
      gameBoard.getField(start).getPiece.get.sList.clear
      gameBoard.getField(start).getPiece.get.sListBlack.clear
      this.movePossible(start, start)
      if (gameBoard.getField(start).getPiece.get.sListBlack.nonEmpty) {
        if (!this.movePossible(start, dest).getRem.isEmpty) {
          undoManager.doStep(new MoveCommand(start, dest, this))
          gameBoard.getField(dest).getPiece.get.sList.clear
          gameBoard.getField(dest).getPiece.get.sListBlack.clear
          this.gameBoard = gameBoard.remove(gameBoard.rowToInt(cap), gameBoard.colToInt(cap))
          this.movePossible(dest, dest)
          if (gameBoard.getField(dest).getPiece.get.sListBlack.isEmpty) gameState = WHITE_TURN
          destTemp = dest
          publish(new FieldChanged)
          publish(new PrintTui)
          cap = ""
        }
      } else gameState = WHITE_TURN
      return
    }
  }



  def movePossible(start: String, dest: String): Mover = {

    var white = 0
    var black = 0
    for {
      row <- 0 until gameBoard.size
      col <- 0 until gameBoard.size
    } {
      if (field(row, col).isSet && field(row, col).getPiece.get.getColor == White) {
        white += 1
      } else if (field(row, col).isSet && field(row, col).getPiece.get.getColor == Black) {
        black += 1
      }
    }
    if (white < 2) {
      gameState = BLACK_WON
      createGameBoard(gameBoard.size)
      gui.winField("Black")
    } else if (black < 2) {
      gameState = WHITE_WON
      createGameBoard(gameBoard.size)
      gui.winField("White")
    }

    if (gameBoard.getField(start).piece.isDefined) {
      gameBoard.movePossible(start, dest)
    } else new Mover(false, "", false)
  }

  def save(): Unit = {
    //fileIo.save(gameBoard)
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")

    implicit val executionContext = system.executionContext

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      method = HttpMethods.POST,
      uri = fileIOServer + "/save",
      entity = gameBoard.jsonToString
    ))
    publish(new FieldChanged)
    //notifyobserver
  }
  
  def load(): Unit = {

    //if (gameBoard.size != oldSize) publish(new GBSizeChanged(gameBoard.size))

    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")

    implicit val executionContext = system.executionContext

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = fileIOServer + "/load"))

    responseFuture
      .onComplete {
        case Failure(_) => sys.error("Failed getting Json")
        case Success(value) => {
          Unmarshal(value.entity).to[String].onComplete {
            case Failure(_) => sys.error("Failed unmarshalling")
            case Success(value) => {
              val loadedGame = gameBoard.jsonToGameBoard()
              this.gameBoard = loadedGame
              publish(new FieldChanged)
              publish(new PrintTui)
            }
          }
        }
      }

  }

  def undo(): Unit = {
    undoManager.undoStep
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def redo(): Unit = {
    undoManager.redoStep
    publish(new FieldChanged)
    publish(new PrintTui)
  }

  def isSet(row: Int, col: Int): Boolean = gameBoard.field(row, col).isSet

  def field(row: Int, col: Int):FieldInterface = gameBoard.field(row,col)

  def gameBoardSize: Int = gameBoard.size

  def statusText: String = GameState.message(gameState)



}