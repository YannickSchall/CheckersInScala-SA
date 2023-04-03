package scala
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule
import controller.controllerComponent.*
import gameboard.GameBoardInterface
import gameboard.gameBoardAdvImpl.GameBoard
import fileIoComponent.fileIoJsonImpl.FileIO
import fileIoComponent.FileIOInterface



class CheckersModule extends AbstractModule with ScalaModule {

  val defaultSize:Int = 10

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[GameBoardInterface].to[GameBoard]
    bind[ControllerInterface].to[controllerBaseImpl.Controller]
    bind[GameBoardInterface].annotatedWithName("8")toInstance(new GameBoard(8))
    bind[GameBoardInterface].annotatedWithName("10")toInstance(new GameBoard(10))
    bind[FileIOInterface].to[FileIO]
  }
}
