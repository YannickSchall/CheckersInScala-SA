package model

import model.gameBoardBaseImpl.Direction.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

class DirectionSpec extends AnyWordSpec {
  "A direction" should {
    val left = Left
    val right = Right
    val uleft = UpLeft
    val uright = UpRight
    val dleft = DownLeft
    val dright = DownRight
    "make Integers out of a String " in {
      left.dir should be ((0, -1))
      right.dir should be ((0, 1))
      uleft.dir should be ((1, -1))
      uright.dir should be ((1, 1))
      dleft.dir should be ((-1, -1))
      dright.dir should be ((-1, 1))
    }
  }
}
