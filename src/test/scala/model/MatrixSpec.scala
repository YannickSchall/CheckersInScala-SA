package model

import model.gameBoardBaseImpl.Color.White
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import model.gameBoardBaseImpl.{Field, Matrix, Piece}
class MatrixSpec extends AnyWordSpec {
  "A Matrix" should {
    val mat = new Matrix[Field](1,Field("", None))
    "have size" in {
      mat.size should be (1)
    }
    "have lines" in {
      mat.lines should be (Vector(Vector(Field("", None))))
    }
    "be able to copy" in {
      mat.copy(mat.lines) should be (mat)
    }
    "be able to retrieve a field" in {
      mat.field(0, 0).isSet should be (false)
    }
    "be able to replace a field" in {
      mat.replaceField(0, 0, Field("A1", Option(Piece.apply("normal", 0, 0, White)))).field(0, 0).isSet should be (true)
    }
  }
}