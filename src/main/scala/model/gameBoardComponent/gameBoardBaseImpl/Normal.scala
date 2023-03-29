package model.gameBoardComponent.gameBoardBaseImpl

import scala.collection.mutable.ListBuffer
import util.Mover
import model.gameBoardComponent.gameBoardBaseImpl.Color.*

case class Normal(state: String = "normal", row: Int, col: Int, getColor: Color) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()
  
  override def toString: String = if (getColor == Black) "\u001B[37mO\u001B[0m" //red
  else "\u001B[30mO\u001B[0m" //blue
  override def posToStr(row: Int, col: Int): String = {
    (col + 65).toChar.toString + (row + 49).toChar.toString
  }
  
  override def fillList(to: String, gameBoard: GameBoard, direction: String, dist_count: Int): ListBuffer[String] = {
    val row_offset: Int = if (getColor == Black) 2 else -2
    val col_offset: Int = if (direction == "right") 2 else -2
    sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + row_offset, col + col_offset).pos
  }

  override def cap_cond(row_offset: Int, col_offset: Int, gameBoard: GameBoard): Boolean = gameBoard.field(row + row_offset, col + col_offset).piece.isDefined && gameBoard.field(row + row_offset, col + col_offset).piece.get.getColor == (if (getColor == Black) White else Black) && gameBoard.field(row + row_offset*2, col + col_offset*2).piece.isEmpty

  override def capturable(direction: String, row_dist: Int, col_dist: Int, gameBoard: GameBoard): Boolean = {

    val Last: Int = gameBoard.size - 1

    def help_bool(row_offset: Int, col_offset: Int): Boolean = cap_cond(row_offset, col_offset, gameBoard)

    (getColor, direction) match {
      case (White, _) if row == 0 || row == 1 => false
      case (Black, _) if row == Last || row == Last - 1 => false
      case (_, "left") if col == 0 || col == 1 => false
      case (_, "right") if col == Last || col == Last - 1 => false
      case (White, "left") => help_bool(-1, -1)
      case (White, "right") => help_bool(-1, 1)
      case (Black, "left") => help_bool(1, -1)
      case (Black, "right") => help_bool(1, 1)
    }
  }
  
  override def getMover(to: String, gameBoard: GameBoard): Mover = {
    
    val Last: Int = gameBoard.size - 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val direction = if (toCol < col) "left" else "right"
    val can_capture = sList.nonEmpty

    def cap(row_offset: Int, col_offset: Int): Boolean = cap_cond(row_offset, col_offset, gameBoard) && to == gameBoard.posToStr(row + row_offset * 2, col + col_offset * 2)

    def no_cap(row_offset: Int, col_offset: Int): Boolean = gameBoard.field(row + row_offset, col + col_offset).piece.isEmpty && to == gameBoard.posToStr(row + row_offset, col + col_offset)
    
    if (!can_capture) {
      (getColor, direction) match {
        case (White, _) if row == 0 => new Mover(false, "", false)
        case (Black, _) if row == Last => new Mover(false, "", false)
        case (_, "left") if col == 0 => new Mover(false, "", false)
        case (_, "right") if col == Last => new Mover(false, "", false)
        case (White, "left") if no_cap(-1, -1) => new Mover(true, "", if toRow == 0 then true else false)
        case (White, "right") if no_cap(-1, 1) => new Mover(true, "", if toRow == 0 then true else false)
        case (Black, "left") if no_cap(1, -1) => new Mover(true, "", if toRow == Last then true else false)
        case (Black, "right") if no_cap(1, 1) => new Mover(true, "", if toRow == Last then true else false)
        case _ => new Mover(false, "", false)
      }
    } else {
      (getColor, direction) match {
        case (White, _) if row == 0 || row == 1 => new Mover(false, "", false)
        case (Black, _) if row == Last || row == Last-1 => new Mover(false, "", false)
        case (_, "left") if col == 0 || col == 1 => new Mover(false, "", false)
        case (_, "right") if col == Last || col == Last-1 => new Mover(false, "", false)
        case (White, "left") if cap(-1, -1) => new Mover(true, posToStr(row - 1, col - 1), if toRow == 0 then true else false)
        case (White, "right") if cap(-1, 1) => new Mover(true, posToStr(row - 1, col + 1), if toRow == 0 then true else false)
        case (Black, "left") if cap(1, -1) => new Mover(true, posToStr(row + 1, col - 1), if toRow == Last then true else false)
        case (Black, "right") if cap(1, 1) => new Mover(true, posToStr(row + 1, col + 1), if toRow == Last then true else false)
        case _ => new Mover(false, "", false)
      }
    }
  }



  override def movePossible(to: String, gameBoard: GameBoard): Mover = {
    val Last: Int = gameBoard.size - 1

    col match {

      case 0 =>
        if (capturable("right", 0, 0, gameBoard)) fillList(to, gameBoard, "right", 0)
        getMover(to, gameBoard)

      case Last =>
        if (capturable("left", 0, 0, gameBoard)) fillList(to, gameBoard,"left", 0)
        getMover(to, gameBoard)

      case _ =>
        if ((col != (Last-1)) && capturable("right", 0, 0, gameBoard)) fillList(to, gameBoard, "right", 0)
        if ((col != 1) && capturable("left", 0, 0,gameBoard)) fillList(to, gameBoard, "left", 0)
        getMover(to, gameBoard)
    }
  }
}