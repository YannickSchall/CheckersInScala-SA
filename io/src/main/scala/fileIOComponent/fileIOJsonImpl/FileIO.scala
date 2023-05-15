package fileIOComponent.fileIOJsonImpl

import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import model.gameBoardBaseImpl.GameBoardInterface
import java.io.{File, PrintWriter}
import fileIOComponent.FileIOInterface

import scala.io.Source
import play.api.libs.json.*

class FileIO @Inject () extends FileIOInterface{

  override def load(): String = {
    val file = scala.io.Source.fromFile("game.json")
    try file.mkString finally file.close()
  }

  override def save(gameAsJson: String): Unit = {
    val pw = new PrintWriter(new File("." + File.separator + "game.json"))
    pw.write(gameAsJson)
    pw.close()
  }

  def gameBoardToJson(gameBoard: GameBoardInterface) = {
    Json.obj(
      "gameBoard" -> Json.obj(
        "size" -> JsNumber(gameBoard.size),
        "fields" -> Json.toJson(
          for {
            row <- 0 until gameBoard.size
            col <- 0 until gameBoard.size
          } yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "field" -> Json.toJson(gameBoard.field(row, col))
            )
          }
        )
      )
    )
  }

  def JsonToGameBoard(jsonString: String) =
    var newGameBoard: GameBoardInterface = gameBoard
    val json: JsValue = Json.parse(jsonString)
    val size = (json \ "gameBoard" \ "size").get.toString.toInt
    /*size match {
      case 8 => gameBoard = injector.instance[GameBoardInterface](Names.named("8"))
      case 10 => gameBoard = injector.instance[GameBoardInterface](Names.named("10"))
      case _ =>
    }*/
    for (index <- 0 until size * size) {
      val row = (json \\ "row") (index).as[Int]
      val col = (json \\ "col") (index).as[Int]
      val field = (json \\ "field") (index).as[Field]
      newGameBoard = newGameBoard.set(row, col, field.piece)
    }
    newGameBoard

}