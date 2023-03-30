package model
import model.gameBoardComponent.gameBoardBaseImpl.{Field, Normal, Piece}
import model.gameBoardComponent.gameBoardBaseImpl.Color.*
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec._
class FieldSpec extends AnyWordSpec {
  "A Field " should {
    val piece = Piece("normal", 2, 1, White)
    val field = Field("A2", None)
    val field2 = Field("B3", Some(piece))
    "have a state" in {
      field.getPos should be ("A2")
      field2.getPos should be ("B3")
    }
    "be a Piece" in {
      field2.getPiece should be (Some(piece))
    }
    "have a toString representation" in {
      field.toString should be (" ")
      field2.toString should be ("\u001B[30mO\u001B[0m")
    }
  }
}