package aview.restAPI

import controller.controllerComponent.ControllerInterface


object UiController {
    /*
    def drop(controller: ControllerInterface, input: String) =
      controller.drop(input)
    */
    def redo(controller: ControllerInterface): Unit =
      controller.redo()

    def undo(controller: ControllerInterface): Unit =
      controller.undo()

    def new8(controller: ControllerInterface) =
      controller.createGameBoard(8)

    def new10(controller: ControllerInterface) =
      controller.createGameBoard(10)

    def gameBoardToJsonToString(controller: ControllerInterface): String =
      controller.gameBoard.jsonToString
}


