package gameboard
import gameboard.gameBoardBaseImpl.Piece
import gameboard.gameBoardBaseImpl.*
import gameboard.{FieldInterface, PieceInterface}
import play.api.libs.json.{Format, JsObject, JsValue, Reads, Writes}
import utils.Mover


trait GameBoardInterface {

  def size: Int

  def getField(pos: String): FieldInterface

  def remove(row: Int, col: Int): GameBoardInterface

  def set(row: Int, col: Int, piece: Option[Piece]): GameBoardInterface

  def field(row: Int, col: Int): FieldInterface

  def colToInt(pos: String): Int

  def rowToInt(pos: String): Int

  def posToStr(row: Int, col: Int): String

  def toString: String

  def getPiece(row: Int, col: Int): Option[PieceInterface]

  def move(start: String, dest: String): GameBoardInterface

  def movePossible(start: String, dest: String): Mover

  def jsonToString: String

  def toJson: JsValue

  def jsonToGameBoard(source: String): GameBoard

  implicit def optionFormat[T: Format]: Format[Option[T]]
  
  implicit val pieceReads: Reads[Piece]

  implicit val fieldReads: Reads[Field]

  //implicit val colorReads: Reads[Color]
  
  implicit val colorWrites: Writes[Color]

  implicit val fieldWrites: Writes[Field]

  implicit val pieceWrites: Writes[Option[Piece]]

}


trait FieldInterface {
  def isSet: Boolean
  def getPos: String
  def getPiece: Option[Piece]
  def toString: String
  def piece: Option[Piece]
}