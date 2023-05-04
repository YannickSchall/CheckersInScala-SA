package utils
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

class incrCommand extends Command {
  var state:Int=0

  override def doStep(): Unit = state+=1

  override def undoStep(): Unit = state-=1

  override def redoStep(): Unit = state+=1
}

class CommandSpec extends AnyWordSpec {
  "A Command" should {
    "do a step" in {
      val command = new incrCommand
      command.state should be (0)
      command.doStep()
      command.state should be (1)
      command.doStep()
      command.state should be (2)
    }
    "undo a step" in {
      val command = new incrCommand
      command.state should be (0)
      command.doStep()
      command.doStep()
      command.state should be (2)
      command.undoStep()
      command.state should be (1)
      command.undoStep()
      command.state should be (0)
    }
    "Redo a Step" in {
      val command = new incrCommand
      command.doStep()
      command.undoStep()
      command.redoStep()
      command.state should be (1)
    }
  }
}
