package scala
import scala.util.{Failure, Success, Try}
import com.google.inject.Guice
import aview.Tui
import aview.gui.Gui
import controller.controllerComponent.ControllerInterface
import restAPI.RestUI
import scala.compiletime.{erasedValue, summonFrom}
import scala.io.StdIn.readLine


object Checkers {
  val injector = Guice.createInjector(new CheckersModule)
  val controller = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new Gui(controller)
  controller.createGameBoard(8)


  def main(args: Array[String]): Unit = {
    var input: String = ""
    Try(RestUI(controller)) match
      case Success(v) => println("View Rest Server is running!")
      case Failure(v) => println("View Rest Server couldn't be started! " + v.getMessage + v.getCause)
    while ({input = readLine(); tui.tuiEntry(input); input != "quit"}) ()
  }

}