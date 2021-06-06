package controller

import model.{GameBoard, Piece}
import util.Command

class MoveCommand(start: String, dest: String, controller: Controller) extends Command {

  var memento: (GameBoard, GameState) = (
    controller.gameBoard,
    controller.gameState
  )

  override def doStep: Unit = {
    memento = (controller.gameBoard, controller.gameState)
    controller.gameBoard = controller.gameBoard.move(start, dest)
  }

  override def undoStep: Unit = {
    val newMemento = (controller.gameBoard, controller.gameState)
    controller.gameBoard = memento._1
    controller.gameState = memento._2
    memento = newMemento
  }

  override def redoStep: Unit = {
    undoStep
  }
}
