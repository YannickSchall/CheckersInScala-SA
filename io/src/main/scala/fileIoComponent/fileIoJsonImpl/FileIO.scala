package fileIoComponent.fileIoJsonImpl

import com.google.inject.Guice
import com.google.inject.name.Names
import gameboard.{FieldInterface, GameBoardInterface, PieceInterface}
import gameboard.gameBoardBaseImpl.*
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import fileIoComponent.FileIOInterface

import scala.io.Source
import play.api.libs.json.*

import java.io.{File, PrintWriter}

class FileIO extends FileIOInterface{
  override def load(): String = {
    val file = scala.io.Source.fromFile("game.json")
    try file.mkString finally file.close()
  }

  override def save(gameAsJson: String): Unit = {
    val pw = new PrintWriter(new File("." + File.separator + "game.json"))
    pw.write(gameAsJson)
    pw.close()
  }
}