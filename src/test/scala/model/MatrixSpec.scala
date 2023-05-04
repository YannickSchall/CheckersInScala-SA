package model
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

class MatrixSpec extends AnyWordSpec {
  "A Matrix" should {
    val mat = new Matrix[Field](1,Field("", None))
    "have size" in {
      mat.size should be (1)
    }
    "have lines" in {
      mat.lines should be (Vector(Vector(Field("", None))))
    }
  }
}