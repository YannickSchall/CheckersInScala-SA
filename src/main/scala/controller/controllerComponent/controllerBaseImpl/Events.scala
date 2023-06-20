package controller.controllerComponent.controllerBaseImpl

import scala.swing.event.Event

class FieldChanged extends Event

case class GBSizeChanged(newSize: Int) extends Event

class PrintTui extends Event
