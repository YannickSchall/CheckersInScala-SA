package model.gameBoardBaseImpl

import util.*
import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import Color.{White, Black}
import model.{FieldInterface, GameBoardInterface}
import utils.Mover
import play.api.libs.json.{Format, JsError, JsNull, JsNumber, JsObject, JsPath, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}

import scala.io.Source

case class
GameBoard @Inject() (fields: Matrix[Field]) extends GameBoardInterface {

  def this(size: Int) = this(new Matrix[Field](size, Field("", None)))

  val size: Int = fields.size

  override def getField(pos: String): Field = {
    field(Integer.parseInt(pos.tail)-1, pos.charAt(0).toInt - 65)
  }

  override def remove(row: Int, col: Int): GameBoard = copy(fields.replaceField(row, col, Field(posToStr(row, col), None)))

  override def set(row: Int, col: Int, piece: Option[Piece]): GameBoard = copy(fields.replaceField(row, col, Field(posToStr(row, col), piece)))

  override def field(row: Int, col: Int): Field = fields.field(row, col)

  override def colToInt(pos: String): Int = pos.charAt(0).toInt - 65

  override def rowToInt(pos: String): Int = Integer.parseInt(pos.tail) - 1

  override def posToStr(row: Int, col: Int): String = (col + 65).toChar.toString + (row+1).toString


  override def toString: String = {
    val lineSeparator = ("+-" + ("--" * (size+1))) + "+\n"
    val line = ("| " + ("o " * size)) + "|\n"
    var box = "\n" + (lineSeparator + (line * size)) + lineSeparator
    for {
      row <- 0 until size
      col <- 0 until size
    } box = box.replaceFirst("o", field(row, col).toString)
    box
  }


  override def getPiece(row: Int, col: Int): Option[Piece] = {
    def pos = posToStr(row, col)
    getField(pos).piece
  }

  override def move(start: String, dest: String): GameBoard = {
    getField(start).piece match {
      case Some(piece) => remove(Integer.parseInt(start.tail)-1, start.charAt(0).toInt - 65).set(Integer.parseInt(dest.tail)-1, dest.charAt(0).toInt - 65, Some(Piece(piece.state, Integer.parseInt(dest.tail)-1, dest.charAt(0).toInt - 65, piece.getColor)))
      case None => print("Field " + start + " is empty"); this
    }
  }

  override def movePossible(start: String, dest: String): Mover = {
    getField(start).piece match {
      case Some(piece) => piece.movePossible(dest, this)
      case _ => new Mover(false, "", false)
    }
  }


  override implicit val pieceReads: Reads[Piece] = (
    (JsPath \ "state").read[String] and
      (JsPath \ "prow").read[Int] and
      (JsPath \ "pcol").read[Int] and
      (JsPath \ "color").read[String]
    )((state, prow, pcol, color) => Piece(state, prow, pcol, if (color == "white") White else Black))

  override implicit val fieldReads: Reads[Field] = (
    (JsPath \ "pos").read[String] and
      (JsPath \ "piece").readNullable[Piece]
    )(Field.apply _)

  /*override implicit val colorReads: Reads[Color] = Reads { json =>
    json.validate[String].flatMap {
      case "white" => JsSuccess(Color.White)
      case "black" => JsSuccess(Color.Black)
      case other => JsError(s"Invalid color: $other")
    }
  }*/

  override implicit val colorWrites: Writes[Color] = new Writes[Color] {
    def writes(color: Color) = Json.obj(
      "color" -> color.color,
    )
  }

  override implicit val fieldWrites: Writes[Field] = new Writes[Field] {
    def writes(field: Field) = Json.obj(
      "pos" -> field.getPos,
      "piece" -> pieceWrites.writes(field.getPiece)
    )
  }

  override implicit val pieceWrites: Writes[Option[Piece]] = new Writes[Option[Piece]] {
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

  def gameBoardToJson = {
    Json.obj(
      "gameBoard" -> Json.obj(
        "size" -> this.size,
        "fields" -> Json.toJson(
          for {
            row <- 0 until this.size
            col <- 0 until this.size
          } yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "field" -> field(row, col)
            )
          }
        )
      )
    )
  }



  override def jsonToString: String =
    Json.prettyPrint(gameBoardToJson)

  override def toJson: JsValue =
    gameBoardToJson

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

  override def jsonToGameBoardSQL(source: String): GameBoard = {
    val json: JsValue = Json.parse(source)
    val size = (json \ "size").get.toString.toInt
    var gb: GameBoard = new GameBoard(size)

    val fields = (json \ "fields").get
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