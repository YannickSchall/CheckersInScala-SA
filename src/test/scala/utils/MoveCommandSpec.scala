package utils
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import controller.controllerComponent.GameState
import controller.controllerComponent.GameState.{GameState, WHITE_TURN}
import model.GameBoardInterface
import utils.Command
import controller.controllerComponent.controllerBaseImpl.*
import model.gameBoardBaseImpl.{Field, GameBoard, GameBoardCreator, Piece}
import model.gameBoardBaseImpl.Color.*

class MoveCommandSpec extends AnyWordSpec with Matchers {
  "MoveCommand" should {
    "move pieces on the game board" in {
      // Create a mock Controller and GameBoardInterface
      val gb = new GameBoard(8)
      val controller = new Controller(gb)
      controller.set(0, 0, Piece.apply("normal", 0, 0, White))


      // Set up the initial state of the game board and controller
      val initialState: (GameBoardInterface, GameState) = (gb, WHITE_TURN)
      controller.gameBoard = gb
      controller.gameState = WHITE_TURN

      // Create a MoveCommand instance
      val moveCommand: Command = new MoveCommand("A1", "C3", controller)


      moveCommand.undoStep()
      controller.gameBoard.field(2,2).isSet should be (false)

      moveCommand.redoStep()
      controller.gameBoard.field(2,2).isSet should be (false)

      moveCommand.doStep()
      controller.set(0, 0, Piece.apply("normal", 0, 0, White))
      controller.move("A1", "C3")
      controller.gameBoard.field(2,2).isSet should be (true)
    }
  }


}