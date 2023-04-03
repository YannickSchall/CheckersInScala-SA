package util
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec._



class UndoManagerSpec extends AnyWordSpec{
  "A UndoManager" should {
    val undo = new UndoManager
    "do a Step, Undo a Step or Redo a Step" in {
      val command = new incrCommand
      command.state should be (0)
      undo.doStep(command)
      command.state should be (1)
      undo.undoStep
      command.state should be (0)
      undo.redoStep
      command.state should be (1)
    }
  }
}
