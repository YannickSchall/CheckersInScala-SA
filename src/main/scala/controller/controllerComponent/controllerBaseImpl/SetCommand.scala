package controller.controllerComponent.controllerBaseImpl
import controller.controllerComponent.GameState.GameState
import model.gameBoardComponent.GameBoardInterface
import util.Command
import model.gameBoardComponent.gameBoardBaseImpl.{GameBoard, Piece}

class SetCommand(start: Int, dest: Int, piece: Option[Piece], controller: Controller) extends Command {

  var memento: (GameBoardInterface, GameState) = (
    controller.gameBoard,
    controller.gameState
  )

  override def doStep(): Unit = {
    memento = (controller.gameBoard, controller.gameState)
    controller.gameBoard = controller.gameBoard.set(start, dest, piece)
  }

  override def undoStep(): Unit = {
    val newMemento = (controller.gameBoard, controller.gameState)
    controller.gameBoard = memento._1
    controller.gameState = memento._2
    memento = newMemento
  }

  override def redoStep(): Unit = {
    undoStep()
  }
}
