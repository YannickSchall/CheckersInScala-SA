package controller
import aview.Tui
import controller.controllerComponent.controllerBaseImpl.{FieldChanged, GBSizeChanged}
import controller.controllerComponent.controllerBaseImpl.{Controller, PrintTui}
import model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*

import scala.swing.event.Event

class EventsSpec extends AnyWordSpec with Matchers {
  "FieldChanged" should {
    "extend Event" in {
      val fieldChanged = new FieldChanged()
      fieldChanged should be(an[Event])
    }
  }

  "GBSizeChanged" should {
    "extend Event" in {
      val gbSizeChanged = GBSizeChanged(10)
      gbSizeChanged should be(an[Event])
    }
  }

  "PrintTui" should {
    "extend Event" in {
      val printTui = new PrintTui()
      printTui should be(an[Event])
    }
  }
}