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

}