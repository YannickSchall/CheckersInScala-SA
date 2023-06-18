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
/*

class FileIO extends FileIOInterface{
  override def load: GameBoardInterface = {
    var gameBoard: GameBoardInterface = null
    val source: String = Source.fromFile("gameBoard.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val size = (json \ "gameBoard" \ "size").get.toString.toInt
    val injector = Guice.createInjector(new CheckersModule)
    size match {
      case 8 => gameBoard = injector.instance[GameBoardInterface](Names.named("8"))
      case 10 => gameBoard = injector.instance[GameBoardInterface](Names.named("10"))
      case _ =>
    }
    for (index <- 0 until size * size) {
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val field = (json \\ "field")(index).as[Field]
      gameBoard = gameBoard.set(row, col, field.piece)
    }
    gameBoard
  }

  override def save(gameBoard: GameBoardInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("gameBoard.json"))
    pw.write(Json.prettyPrint(gameBoardToJson(gameBoard)))
    pw.close()
  }

  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]]{
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
    ) (Field.apply _)

  implicit val colorWrites: Writes[Color] = new Writes[Color] {
    def writes(color: Color) = JsString(color.color)
  }

  implicit val colorReads: Reads[Color] = Reads { json =>
    json.validate[String].flatMap {
      case "white" => JsSuccess(Color.White)
      case "black" => JsSuccess(Color.Black)
      case other => JsError(s"Invalid color: $other")
    }
  }

  implicit val fieldWrites: Writes[FieldInterface] = new Writes[FieldInterface] {
    def writes(field: FieldInterface) = Json.obj(
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
}
*/