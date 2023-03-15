package aview.gui

import java.awt.Color

import scala.swing._
import scala.swing.event._
import controller.controllerComponent.ControllerInterface
import javax.swing.{BorderFactory, Icon, ImageIcon}
import model.gameBoardComponent.gameBoardBaseImpl.Piece
import java.io.File

import scala.Checkers.{gui, injector}

class FieldPanel(row: Int, col: Int, controller: ControllerInterface, backgroundColor: Color) extends FlowPanel {
  vGap = 0
  hGap = 0
  var color: String = "white"
  def myField = controller.field(row, col)

  val dir: String = new File("").getAbsolutePath
  val pieceBlackS = new ImageIcon(dir+"\\src\\main\\resources\\pieceBlackSmall.png")
  val pieceWhiteS = new ImageIcon(dir+"\\src\\main\\resources\\pieceWhiteSmall.png")
  val queenBlackS = new ImageIcon(dir+"\\src\\main\\resources\\queenBlackSmall.png")
  val queenWhiteS = new ImageIcon(dir+"\\src\\main\\resources\\queenWhiteSmall.png")
  val pieceBlack = new ImageIcon(dir+"\\src\\main\\resources\\pieceBlack.png")
  val pieceWhite = new ImageIcon(dir+"\\src\\main\\resources\\pieceWhite.png")
  val queenBlack = new ImageIcon(dir+"\\src\\main\\resources\\queenBlack.png")
  val queenWhite = new ImageIcon(dir+"\\src\\main\\resources\\queenWhite.png")
  val blankS = new ImageIcon(dir+"\\src\\main\\resources\\blankS.png")
  val blank = new ImageIcon(dir+"\\src\\main\\resources\\blank.png")


  def fieldText(): String = {
    color = "white"
    if (myField.isSet) {
      if (controller.getPiece(row, col).get.getColor == "white") print("")
      controller.getPiece(row, col).get.toString
    } else " "
  }


  val label: Label =
    new Label {
      pieceMatcher(this)
    }


  def pieceMatcher(labelX: Label): Unit = {
    var fcolor = ""
    var fstate = ""
    if (myField.getPiece.isDefined) fcolor = myField.getPiece.get.getColor
    if (myField.getPiece.isDefined) fstate = myField.getPiece.get.state

    fcolor match {

      case "black" => if (fstate == "normal") labelX.icon = {
        if (controller.gameBoardSize == 10) pieceBlackS
        else pieceBlack
      } else labelX.icon = {
        if (controller.gameBoardSize == 10) queenBlackS
        else queenBlack
      }

      case "white" => if (fstate == "normal") labelX.icon = {
        if (controller.gameBoardSize == 10) pieceWhiteS
        else pieceWhite
      } else labelX.icon = {
        if (controller.gameBoardSize == 10) queenWhiteS
        else queenWhite
      }
      case _ => if (controller.gameBoardSize == 10) {
        labelX.icon = blankS} else labelX.icon = blank
    }
  }

  val field: BoxPanel = new BoxPanel(Orientation.NoOrientation) {
    listenTo(mouse.clicks)
    reactions += {
      case e: MouseClicked =>
        if (gui.flagTest == 0) {
          if (myField.getPiece.isDefined) {
            gui.fieldStart = myField.getPos
            gui.flagTest = 1
            gui.colorFlag = this
            field.background = new Color(150, 150, 150)
          }
        } else {
          gui.flagTest = 0
          gui.fieldDest = myField.getPos
          if (gui.bgcol == "rb") gui.colorFlag.background = new Color(118,0,0)
          if (gui.bgcol == "wb") gui.colorFlag.background = new Color(250,250,250)
        }
        if (!(gui.fieldDest == "") && gui.flagTest == 0) {
          if (controller.movePossible(gui.fieldStart, gui.fieldDest).getBool) {
            val row = Integer.parseInt(gui.fieldDest.tail)-1
            val col = gui.fieldDest.charAt(0).toInt - 65
            var rem = false
            var which = ""
            if (controller.movePossible(gui.fieldStart, gui.fieldDest).getRem.nonEmpty && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.charAt(0).toString) rem = true; which = controller.movePossible(gui.fieldStart, gui.fieldDest).getRem
            if (controller.movePossible(gui.fieldStart, gui.fieldDest).getQ && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.charAt(0).toString) {
              print(controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.charAt(0).toString)
              controller.move(gui.fieldStart, gui.fieldDest)
              controller.set(row, col, Piece("queen", row, col, controller.getPiece(row, col).get.getColor))
              if (rem) controller.remove(Integer.parseInt(which.tail)-1, which.charAt(0).toInt - 65)
            } else controller.move(gui.fieldStart, gui.fieldDest)
          } else print("Move not possible\n")
        }
    }

    pieceMatcher(label)
    contents += label

    preferredSize = new Dimension(100, 100)
    background = backgroundColor

    if (controller.gameBoardSize == 8) {
      border = BorderFactory.createEmptyBorder(0,9,13,0)
    } else border = BorderFactory.createEmptyBorder(-10,19,20,0)
    repaint()
  }

  def redraw = {
    contents.clear
    pieceMatcher(label)
    contents += field
    repaint()
  }
}
