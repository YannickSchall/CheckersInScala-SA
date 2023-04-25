import java.awt.{Color, Dimension, GridLayout, Toolkit}
import java.io.File

import scala.swing.{Button, _}
import scala.swing.event._
import javax.swing.{BorderFactory, Icon, ImageIcon, JButton, JOptionPane, JPanel}

class Gui(uiController: UiController) extends Frame {
  title = "Checkers"
  resizable = false
  minimumSize = new Dimension(800, 800)
  centerOnScreen()
  var fields = Array.ofDim[FieldPanel](uiController.gameBoardSize, uiController.gameBoardSize)
  var flagTest = 0
  var fieldStart = ""
  var fieldDest = ""
  var colorFlag = new BoxPanel(Orientation.NoOrientation)
  var bgcol = "rb"

  vGap = 0
  hGap = 0
  var color: String = "white"

  def myField = controller.field(row, col)


  val dir: String = new File("").getAbsolutePath
  val pieceBlackS = new ImageIcon(dir + "\\src\\main\\resources\\pieceBlackSmall.png")
  val pieceWhiteS = new ImageIcon(dir + "\\src\\main\\resources\\pieceWhiteSmall.png")
  val queenBlackS = new ImageIcon(dir + "\\src\\main\\resources\\queenBlackSmall.png")
  val queenWhiteS = new ImageIcon(dir + "\\src\\main\\resources\\queenWhiteSmall.png")
  val pieceBlack = new ImageIcon(dir + "\\src\\main\\resources\\pieceBlack.png")
  val pieceWhite = new ImageIcon(dir + "\\src\\main\\resources\\pieceWhite.png")
  val queenBlack = new ImageIcon(dir + "\\src\\main\\resources\\queenBlack.png")
  val queenWhite = new ImageIcon(dir + "\\src\\main\\resources\\queenWhite.png")
  val blankS = new ImageIcon(dir + "\\src\\main\\resources\\blankS.png")
  val blank = new ImageIcon(dir + "\\src\\main\\resources\\blank.png")


  val dir: String = new File("").getAbsolutePath
  iconImage = new ImageIcon(dir+"\\src\\main\\resources\\icon.png").getImage

  def gameBoardPanel = new GridPanel(uiController.gameBoardSize, uiController.gameBoardSize) {
    border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
    for {
      row <- 0 until uiController.gameBoardSize
      col <- 0 until uiController.gameBoardSize
    } {
      var fieldPanel = new FieldPanel(row, col, uiController, if((row+col)%2==0) new Color(118,0,0) else new Color(0,0,0))
      if (bgcol == "wb") fieldPanel = new FieldPanel(row, col, uiController, if((row+col)%2==0) new Color(250,250,250) else new Color(0,0,0))
      fields(row)(col) = fieldPanel
      contents += fieldPanel
    }
    background = new Color(40, 40, 40)

  }

  def labelRowL = new GridPanel(uiController.gameBoardSize, 1) {
    if (uiController.gameBoardSize == 10) border = BorderFactory.createEmptyBorder(0, 3, 0, 0)
    if (uiController.gameBoardSize == 8) border = BorderFactory.createEmptyBorder(0, 2, 0, -1)
    background = new Color(40, 40, 40)
    for (i <- Range(1, uiController.gameBoardSize + 1)) {
      contents += new Label {
        text = i.toString
        foreground = new Color(230, 230, 230)
        font = new Font("Arial", 1, 15)
        preferredSize = new Dimension(17, 0)
      }
    }
  }

  def labelRowR = new GridPanel(uiController.gameBoardSize, 1) {
    if (uiController.gameBoardSize == 10) border = BorderFactory.createEmptyBorder(0, -1, 0, 2)
    if (uiController.gameBoardSize == 8) border = BorderFactory.createEmptyBorder(0, -1, 0, 2)
    background = new Color(40, 40, 40)
    for (i <- Range(1, uiController.gameBoardSize + 1)) {
      contents += new Label {
        text = i.toString
        foreground = new Color(230, 230, 230)
        font = new Font("Arial", 1, 15)
        preferredSize = new Dimension(17, 0)
      }
    }
  }

  def labelColT = new GridPanel(1, uiController.gameBoardSize - 1) {
    if (uiController.gameBoardSize == 10) border = BorderFactory.createEmptyBorder(2, 20, 0, 20)
    if (uiController.gameBoardSize == 8) border = BorderFactory.createEmptyBorder(2, 20, -2, 20)
    background = new Color(40, 40, 40)
    for (i <- Range(65, uiController.gameBoardSize + 65)) {
      contents += new Label {
        text = i.toChar.toString
        foreground = new Color(230, 230, 230)
        font = new Font("Arial", 1, 15)
        preferredSize = new Dimension(0, 17)
      }
    }
  }

  def labelColB = new GridPanel(1, uiController.gameBoardSize - 1) {
    if (uiController.gameBoardSize == 10) border = BorderFactory.createEmptyBorder(0, 20, 0, 20)
    if (uiController.gameBoardSize == 8) border = BorderFactory.createEmptyBorder(-1, 20, 0, 20)
    background = new Color(40, 40, 40)
    for (i <- Range(65, uiController.gameBoardSize + 65)) {
      contents += new Label {
        text = i.toChar.toString
        foreground = new Color(230, 230, 230)
        font = new Font("Arial", 1, 15)
        preferredSize = new Dimension(0, 17)
      }
    }
  }

  contents = new BorderPanel {
    add(gameBoardPanel, BorderPanel.Position.Center)
    add(labelColT, BorderPanel.Position.North)
    add(labelColB, BorderPanel.Position.South)
    add(labelRowR, BorderPanel.Position.East)
    add(labelRowL, BorderPanel.Position.West)
  }

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New") { uiController.createGameBoard(uiController.gameBoardSize) })
      contents += new MenuItem(Action("Quit") { System.exit(0) })
    }
    contents += new Menu("Edit") {

        contents += new Menu("Debugging") {
          contents += new MenuItem(Action("Undo") { uiController.undo() })
          contents += new MenuItem(Action("Redo") { uiController.redo() })
        }
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new Menu("Change Colors") {
        contents += new MenuItem(Action("White/Black") { changeColor("wb") })
        contents += new MenuItem(Action("Red/Black") { changeColor("rb") })
      }
      contents += new Menu("Resize") {
        contents += new MenuItem(Action("Size 8*8") { changeSize(8) })
        contents += new MenuItem(Action("Size 10*10") { changeSize(10) })
      }
    }
  }
  visible = true
  redraw

  reactions += {
    case event: GBSizeChanged => resize(event.newSize)
    case event: FieldChanged => redraw
  }

  def resize(gameBoardSize: Int) = {
    fields = Array.ofDim[FieldPanel](uiController.gameBoardSize, uiController.gameBoardSize)
    contents = new BorderPanel {
      add(gameBoardPanel, BorderPanel.Position.Center)
      add(labelColT, BorderPanel.Position.North)
      add(labelColB, BorderPanel.Position.South)
      add(labelRowR, BorderPanel.Position.East)
      add(labelRowL, BorderPanel.Position.West)
    }
  }

  def redraw = {
    for {
      row <- 0 until uiController.gameBoardSize
      column <- 0 until uiController.gameBoardSize
    } fields(row)(column).redraw
    repaint()
    visible = true
  }

  def winField(color: String): Unit = {
    Dialog.showMessage(contents.head, color + " has won the game!", title="Game finished")
  }


  def changeColor(color: String): Unit = {
    val dialogButton = JOptionPane.YES_NO_OPTION
    val dialogResult = JOptionPane.showConfirmDialog(null, "When you change the color, your game will be reset!\nAre you ok with that?", "Warning", dialogButton)
    if (dialogResult == 0) {
      bgcol = color
      uiController.createGameBoard(uiController.gameBoardSize)
    }
  }

  def changeSize(size: Int): Unit = {
    val dialogButton = JOptionPane.YES_NO_OPTION
    val dialogResult = JOptionPane.showConfirmDialog(null, "When you change the size, your game will be reset!\nAre you ok with that?", "Warning", dialogButton)
    if (dialogResult == 0) {
      uiController.resize(size)
    }
  }


  def fieldText(): String = {
    color = "white"
    if (myField.isSet) {
      if (controller.getPiece(row, col).get.getColor == White) print("")
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
    if (myField.getPiece.isDefined) fcolor = myField.getPiece.get.getColor.toString
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
        labelX.icon = blankS
      } else labelX.icon = blank
    }
  }

  val field: BoxPanel = new BoxPanel(Orientation.NoOrientation) {
    listenTo(mouse.clicks)
    reactions += {
      case e: MouseClicked =>
        if (flagTest == 0) {
          if (myField.getPiece.isDefined) {
            fieldStart = myField.getPos
            flagTest = 1
            colorFlag = this
            field.background = new Color(150, 150, 150)
          }
        } else {
          flagTest = 0
          fieldDest = myField.getPos
          if (bgcol == "rb") colorFlag.background = new Color(118, 0, 0)
          if (bgcol == "wb") colorFlag.background = new Color(250, 250, 250)
        }
        if (!(fieldDest == "") && flagTest == 0) {
          if (controller.movePossible(gui.fieldStart, gui.fieldDest).getBool) {
            val row = Integer.parseInt(fieldDest.tail) - 1
            val col = fieldDest.charAt(0).toInt - 65
            var rem = false
            var which = ""
            if (controller.movePossible(gui.fieldStart, gui.fieldDest).getRem.nonEmpty && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.toString.charAt(0).toString) rem = true;
            which = controller.movePossible(gui.fieldStart, gui.fieldDest).getRem
            if (controller.movePossible(gui.fieldStart, gui.fieldDest).getQ && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.toString.charAt(0).toString) {
              print(controller.getPiece(Integer.parseInt(gui.fieldStart.tail) - 1, gui.fieldStart.charAt(0).toInt - 65).get.getColor.toString.charAt(0).toString)
              controller.move(gui.fieldStart, gui.fieldDest)
              controller.set(row, col, Piece("queen", row, col, controller.getPiece(row, col).get.getColor))
              if (rem) controller.remove(Integer.parseInt(which.tail) - 1, which.charAt(0).toInt - 65)
            } else controller.move(gui.fieldStart, gui.fieldDest)
          } else print("Move not possible\n")
        }
    }

    pieceMatcher(label)
    contents += label

    preferredSize = new Dimension(100, 100)
    background = backgroundColor

    if (controller.gameBoardSize == 8) {
      border = BorderFactory.createEmptyBorder(0, 9, 13, 0)
    } else border = BorderFactory.createEmptyBorder(-10, 19, 20, 0)
    repaint()
  }

  def redraw = {
    contents.clear
    pieceMatcher(label)
    contents += field
    repaint()
  }
}