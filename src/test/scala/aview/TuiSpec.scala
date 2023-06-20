package aview
import aview.Tui
import controller.controllerComponent.controllerBaseImpl.Controller
import model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

class TuiSpec extends AnyWordSpec {
  "A tui" should {
    val controller = new Controller(new GameBoard(8))
    val tui = new Tui(controller)
    val testGB8 = new GameBoardCreator(8).createBoard()
    val testGB10 = new GameBoardCreator(10).createBoard()
    "create us a 8x8 gameboard when typing 'new8' " in {
      tui.tuiEntry("new 8")
      controller.gameBoard should be (testGB8)
    }
    "create us a 10x10 gameboard when typing 'new10' " in {
      tui.tuiEntry("new 10")
      controller.gameBoard should be (testGB10)
    }
    "have a size" in {
      tui.size should be (10)
    }
  }
}