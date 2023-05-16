package fileIOComponent.fileIOJsonImpl

import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import fileIOComponent.IOInterface
import java.io.{File, PrintWriter}
import fileIOComponent.dbImpl.Slick.SlickDBCheckers
import fileIOComponent.model.{FieldInterface, GameBoardInterface}
import fileIOComponent.model.gameBoardBaseImpl.Color.{Black, White}
import fileIOComponent.model.gameBoardBaseImpl.{Color, Field, GameBoard, Piece}

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

  implicit val pieceReads: Reads[Piece] = (
    (JsPath \ "state").read[String] and
      (JsPath \ "prow").read[Int] and
      (JsPath \ "pcol").read[Int] and
      (JsPath \ "color").read[String]
    )((state, prow, pcol, color) => Piece(state, prow, pcol, if (color == "white") White else Black))

  implicit val fieldReads: Reads[Field] = (
    (JsPath \ "pos").read[String] and
      (JsPath \ "piece").readNullable[Piece]
    )(Field.apply _)

  implicit val colorWrites: Writes[Color] = new Writes[Color] {
    def writes(color: Color) = Json.obj(
      "color" -> color.color,
    )
  }

  implicit val fieldWrites: Writes[FieldInterface] = new Writes[FieldInterface] {
    def writes(field: FieldInterface) = Json.obj(
      "pos" -> field.getPos,
      "piece" -> pieceWrites.writes(field.getPiece)
    )
  }

  implicit val pieceWrites: Writes[Option[Piece]] = new Writes[Option[Piece]] {
    def writes(piece: Option[Piece]) = {
      if (piece.isDefined) {
        Json.obj(
          "state" -> piece.get.state,
          "prow" -> piece.get.row,
          "pcol" -> piece.get.col,
          "color" -> piece.get.getColor.color
        )
      } else {
        JsNull
      }
    }
  }

  override def gameBoardToJson(gb: GameBoardInterface): String = {
    Json.prettyPrint(
      Json.obj(
        "gameBoard" -> Json.obj(
          "size" -> gb.size,
          "fields" -> Json.toJson(
            for {
              row <- 0 until gb.size
              col <- 0 until gb.size
            } yield {
              Json.obj(
                "row" -> row,
                "col" -> col,
                "field" -> gb.field(row, col)
              )
            }
          )
        )
      )
    )
  }
  
  override def jsonToGameBoard(source: String): GameBoard = {
    val json: JsValue = Json.parse(source)
    val size = (json \ "gameBoard" \ "size").get.toString.toInt
    var gb: GameBoard = new GameBoard(size)

    val fields = (json \ "gameBoard" \ "fields").get
    for (index <- 0 until size * size) {
      val row = fields(index)("row").as[Int]
      val col = fields(index)("col").as[Int]
      val check = fields(index)("field")("piece")
      val piece = if (check.toString != "null") check.as[Piece] else null
      gb = gb.set(row, col, Option(piece))
    }
    gb
  }

}