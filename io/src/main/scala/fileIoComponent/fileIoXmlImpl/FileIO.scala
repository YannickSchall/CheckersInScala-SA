package fileIoComponent.fileIoXmlImpl
import fileIoComponent.FileIOInterface
import com.google.inject.Guice
import com.google.inject.name.Names
import gameboard.gameBoardBaseImpl.*
import gameboard.GameBoardInterface
import gameboard.gameBoardBaseImpl.Piece
import gameboard.gameBoardBaseImpl.Color
import gameboard.gameBoardBaseImpl.Color.{Black, White}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import java.io.{File, PrintWriter}
import util.*
import scala.xml.{Elem, PrettyPrinter}
/*
class FileIO extends FileIOInterface {
  def load(): String = {
    val file = scala.io.Source.fromFile("game.xml")
    try file.mkString finally file.close()

  }

  def save(gameAsJson: String) = {
    val pw = new PrintWriter(new File("." + File.separator + "game.xml"))
    pw.write(gameAsJson)
    pw.close
  }


}
*/