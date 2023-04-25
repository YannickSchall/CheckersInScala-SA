package gameboard.gameBoardBaseImpl
import util.*
import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import gameboard.{FieldInterface, GameBoardInterface}
import play.api.libs.json.{Format, JsError, JsNull, JsNumber, JsPath, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import utils.Mover

import scala.io.Source

case class
GameBoard @Inject() (fields: Matrix[Field]) extends GameBoardInterface {

  def this(size: Int) = this(new Matrix[Field](size, Field("", None)))

  val size: Int = fields.size

  def getField(pos: String): Field = {
    field(Integer.parseInt(pos.tail)-1, pos.charAt(0).toInt - 65)
  }

  def remove(row: Int, col: Int): GameBoard = copy(fields.replaceField(row, col, Field(posToStr(row, col), None)))

  def set(row: Int, col: Int, piece: Option[Piece]): GameBoard = copy(fields.replaceField(row, col, Field(posToStr(row, col), piece)))

  def field(row: Int, col: Int): Field = fields.field(row, col)

  def colToInt(pos: String): Int = pos.charAt(0).toInt - 65

  def rowToInt(pos: String): Int = Integer.parseInt(pos.tail) - 1

  def posToStr(row: Int, col: Int): String = (col + 65).toChar.toString + (row+1).toString


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


  def getPiece(row: Int, col: Int): Option[Piece] = {
    def pos = posToStr(row, col)
    getField(pos).piece
  }

  def move(start: String, dest: String): GameBoard = {
    getField(start).piece match {
      case Some(piece) => remove(Integer.parseInt(start.tail)-1, start.charAt(0).toInt - 65).set(Integer.parseInt(dest.tail)-1, dest.charAt(0).toInt - 65, Some(Piece(piece.state, Integer.parseInt(dest.tail)-1, dest.charAt(0).toInt - 65, piece.getColor)))
      case None => print("Field " + start + " is empty"); this
    }
  }

  def movePossible(start: String, dest: String): Mover = {
    getField(start).piece match {
      case Some(piece) => piece.movePossible(dest, this)
      case _ => new Mover(false, "", false)
    }
  }

  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]] {
    override def reads(json: JsValue): JsResult[Option[T]] = json.validateOpt[T]

    override def writes(o: Option[T]): JsValue = o match {
      case Some(t) ⇒ implicitly[Writes[T]].writes(t)
      case None ⇒ JsNull
    }
  }


  implicit val pieceReads: Reads[Piece] = (
    (JsPath \ "state").read[String] and
      (JsPath \ "prow").read[Int] and
      (JsPath \ "pcol").read[Int] and
      (JsPath \ "color").read[Color]
    )(Piece.apply _)

  implicit val fieldReads: Reads[Field] = (
    (JsPath \ "pos").read[String] and
      (JsPath \ "piece").readNullable[Piece]
    )(Field.apply _)

  implicit val colorReads: Reads[Color] = Reads { json =>
    json.validate[String].flatMap {
      case "white" => JsSuccess(Color.White)
      case "black" => JsSuccess(Color.Black)
      case other => JsError(s"Invalid color: $other")
    }
  }

  implicit val colorWrites: Writes[Color] = new Writes[Color] {
    def writes(color: Color) = JsString(color.color)
  }

  implicit val fieldWrites: Writes[Field] = new Writes[Field] {
    def writes(field: Field) = Json.obj(
      "pos" -> field.getPos,
      "piece" -> pieceWrites.writes(field.getPiece.get)
    )
  }

  implicit val pieceWrites: Writes[Piece] = new Writes[Piece] {
    def writes(piece: Piece) = Json.obj(
      "state" -> piece.state,
      "prow" -> piece.row,
      "pcol" -> piece.col,
      "color" -> colorWrites.writes(piece.getColor)
    )
  }


  def gameBoardToJson = {
    Json.obj(
      "gameBoard" -> Json.obj(
        "size" -> JsNumber(this.size),
        "fields" -> Json.toJson(
          for {
            row <- 0 until this.size
            col <- 0 until this.size
          } yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "field" -> Json.toJson(this.field(row, col))
            )
          }
        )
      )
    )
  }

  def jsonToString: String =
    Json.prettyPrint(gameBoardToJson)

  def toJson: JsValue =
    gameBoardToJson

  override def jsonToGameBoard(gameBoard: GameBoardInterface): GameBoardInterface = {
    var gameBoard: GameBoardInterface = null
    val source: String = Source.fromFile("gameBoard.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val size = (json \ "gameBoard" \ "size").get.toString.toInt
    for (index <- 0 until size * size) {
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val field = (json \\ "field")(index).as[Field]
      gameBoard = gameBoard.set(row, col, field.piece)
    }
    gameBoard
  }
}