package fileIOComponent.fileIOJsonImpl

import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import java.io.{File, PrintWriter}
import fileIOComponent.IOInterface

import scala.io.Source
import play.api.libs.json.*

class IO @Inject () extends IOInterface{

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