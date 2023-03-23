package model.gameBoardComponent.gameBoardBaseImpl

import scala.collection.mutable.ListBuffer
import util.Mover

case class Normal(state: String = "normal", row: Int, col: Int, getColor: String) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()

  override def toString: String = if (getColor == "black") "\u001B[37mO\u001B[0m" //red
  else "\u001B[30mO\u001B[0m" //blue
  override def posToStr(row: Int, col: Int): String = {
    (col + 65).toChar.toString + (row + 49).toChar.toString
  }


  //def mvePossible(to: String, gameBoard: GameBoard, row, col, piece, color)
  // def white move(mvePossible)
  /*
  def mvePossible(piece.color): Mover = {
  col match {
    if (
       boolean capturable(row, col, gameboard)
     )
       fillList()
       returnMover()

    }
  }

  def whtMvePossible(): Mover = mvePossible(white)_
  def whtMvePossible(): Mover = mvePossible(white)_

  def fillList(to: String, gameBoard): Boolean = {
    sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col + 2).pos
  }
  */

  def fillList(to: String, gameBoard: GameBoard, row_offset: Int, col_offset: Int): ListBuffer[String] = {
    sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + row_offset, col + col_offset).pos
  }


  //if ((row != 0 && row != 1) && (col != 0 && col != 1) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == "black" && gameBoard.field(row - 2, col - 2).piece.isEmpty) {


  def capturable(direction: String, gameBoard: GameBoard): Boolean = {

    val Last: Int = gameBoard.size - 1
    def help_bool(row_offset: Int, col_offset: Int): Boolean = gameBoard.field(row + row_offset, col + col_offset).piece.isDefined && gameBoard.field(row + row_offset, col + col_offset).piece.get.getColor == (if (getColor == "black") "white" else "black") && gameBoard.field(row + row_offset*2, col + col_offset*2).piece.isEmpty

    (getColor, direction) match {
      case ("white", _) if row == 1 || row == 0 => false
      case ("black", _) if row == Last || row == Last-1 => false
      case ("white", "left") => help_bool(-1, -1)
      case ("white", "right") => help_bool(-1, 1)
      case ("black", "left") => help_bool(1, -1)
      case ("black", "right") => help_bool(1, 1)
    }
  }

  override def whiteMovePossible(to: String, gameBoard: GameBoard): Mover = {
    val Last: Int = gameBoard.size - 1

    col match {

      case 0 =>
        if(capturable("right", gameBoard)) sList = fillList( to,gameBoard, -2, 2)


        if (sList.isEmpty) {
          if (row != 0 && gameBoard.field(row - 1, col + 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row - 1, col + 1)) {
              return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == "black") {
          if ((row != 0 && row != 1) && to == gameBoard.posToStr(row - 2, col + 2) && gameBoard.field(row - 2, col + 2).piece.isEmpty) {
            return new Mover(true, posToStr(row - 1, col + 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)


      case Last =>

        if ((row != 0 && row != 1) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == "black" && gameBoard.field(row - 2, col - 2).piece.isEmpty) {
          //sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col - 2).pos
          sList = fillList( to,gameBoard, -2, -2)
        }

        if (sList.isEmpty) {
          if (row != 0 && gameBoard.field(row - 1, col - 1).piece.isEmpty) { // checkMove
            if (to == gameBoard.posToStr(row - 1, col - 1)) { //  checkTo
              if (Integer.parseInt(to.tail) - 1 == 0) { // checkTail
                return new Mover(true, "", true)
              } else return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == "black") {
          if ((row != 0 && row != 1) && to == gameBoard.posToStr(row - 2, col - 2) && gameBoard.field(row - 2, col - 2).piece.isEmpty) {
            return new Mover(true, posToStr(row - 1, col - 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)



      case _ =>

        if ((col != 1) && capturable("left", gameBoard)) {
          sList = fillList( to,gameBoard, -2, -2)
        }

        if ((col != Last && col != (Last-1)) && capturable("right", gameBoard)) {
          //sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col + 2).pos
          sList = fillList( to,gameBoard, -2, 2)
        }


        /*
        def returnMover(to: String, gameboard: GameBoard, row_offset: Int, col_offset: Int): Mover = {
          if ()

        }

        def actuallyEmpty(Gameboard: GameBoard, row_offset: Int, col_offset: Int): Boolean = {
            if (true) return new Mover(false, "", false)
        }

        def matchesPos(to: String) : Boolean = {
          if (to == gameBoard.posToStr(row - 1, col - 1))
            actuallyEmpty()
        }


        var isMoveValid = false
        var shouldPromote = false
        var emptyString = ""

        def ifFall(): Mover = {
          if (sList.isEmpty) return checkM
        }


        def checkMove(gameBoard: GameBoard, row_offset: Int, col_offset: Int): = Mover {
          if ( row != 0 && gameBoard.field(row + row_offset, col + col_offset ).piece.isEmpty()) return checkT0(to, row_offset, col_offset)

          else return new Mover(false, "", false)
        }

        def checkTo(to: String, row_offset, col_offset): = Mover {
            if (to == gameBoard.posToStr(row - row_offset, col - col_offset)) return checkTaill(to)
            else return new Mover(true, "", false)
        }

        def checkTail(to: String, isValid, emptyString, isQ): = Mover
            if (Integer.parseInt(to.tail) - 1 == 0) return new Mover(true, "", true)
        }



         */


        if (sList.isEmpty) {
          if (row != 0 && gameBoard.field(row - 1, col - 1).piece.isEmpty && to == gameBoard.posToStr(row - 1, col - 1)) {
            if (Integer.parseInt(to.tail) - 1 == 0) {
              return new Mover(true, "", true)
            } else return new Mover(true, "", false)
          }
          else if (row != 0 && gameBoard.field(row - 1, col + 1).piece.isEmpty && to == gameBoard.posToStr(row - 1, col + 1)) {
            if (Integer.parseInt(to.tail) - 1 == 0) {
              return new Mover(true, "", true)
            } else return new Mover(true, "", false)
          }
          new Mover(false, "", false)
        }
        else if ((row != 0 && row != 1 && col != 1 && col != 0) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == "black" && gameBoard.field(row - 2, col - 2).piece.isEmpty && to == gameBoard.posToStr(row - 2, col - 2)) {
          if (Integer.parseInt(to.tail) - 1 == 0) {
            new Mover(true, posToStr(row - 1, col - 1), true)
          } else new Mover(true, posToStr(row - 1, col - 1), false)
        }
        else if ((row != 0 && row != 1 && col != Last-1 && col != Last) && gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == "black" && gameBoard.field(row - 2, col + 2).piece.isEmpty && to == gameBoard.posToStr(row - 2, col + 2)) {
          if (Integer.parseInt(to.tail) - 1 == 0) {
            new Mover(true, posToStr(row - 1, col + 1), true)
          } else new Mover(true, posToStr(row - 1, col + 1), false)
        }
        else new Mover(false, "", false)
    }
  }


  override def blackMovePossible(to: String, gameBoard: GameBoard): Mover = {
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>

        if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col + 2).pos
        }

        if (sListBlack.isEmpty) {
          if (row != Last && gameBoard.field(row + 1, col + 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row + 1, col + 1)) {
              if (Integer.parseInt(to.tail) - 1 == Last) {
                return new Mover(true, "", true)
              }
              return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == "white") {
          if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && to == gameBoard.posToStr(row + 2, col + 2) && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
            return new Mover(true, posToStr(row + 1, col + 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)



      case Last =>

        if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col - 2).pos
        }

        if (sListBlack.isEmpty) {
          if (row != Last && gameBoard.field(row + 1, col - 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row + 1, col - 1)) {
              return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == "white") {
          if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && to == gameBoard.posToStr(row + 2, col - 2) && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
            return new Mover(true, posToStr(row + 1, col - 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)


      case _ =>

        if ((col != 0 && col != 1 && row != Last && row != Last-1) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col - 2).pos
        }

        if ((col != Last && col != Last-1 && row != Last && row != Last-1) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col + 2).pos
        }

        if (sListBlack.isEmpty) {
          if (row != Last && gameBoard.field(row + 1, col - 1).piece.isEmpty && to == gameBoard.posToStr(row + 1, col - 1)) {
            if (Integer.parseInt(to.tail) - 1 == Last) {
              return new Mover(true, "", true)
            } else return new Mover(true, "", false)
          }

          else if (row != Last && gameBoard.field(row + 1, col + 1).piece.isEmpty && to == gameBoard.posToStr(row + 1, col + 1)) {
            if (Integer.parseInt(to.tail) - 1 == Last) {
              return new Mover(true, "", true)
            } else return new Mover(true, "", false)
          }
          new Mover(false, "", false)
        }

        else if ((row != Last && row != Last-1 && col != 0 && col != 1) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col - 2).piece.isEmpty && to == gameBoard.posToStr(row + 2, col - 2)) {
          if (Integer.parseInt(to.tail) - 1 == Last) {
            new Mover(true, posToStr(row + 1, col - 1), true)
          } else new Mover(true, posToStr(row + 1, col - 1), false)
        }

        else if ((row != Last && row != Last-1 && col != Last && col != Last-1) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == "white" && gameBoard.field(row + 2, col + 2).piece.isEmpty && to == gameBoard.posToStr(row + 2, col + 2)) {
          if (Integer.parseInt(to.tail) - 1 == Last) {
            new Mover(true, posToStr(row + 1, col + 1), true)
          } else new Mover(true, posToStr(row + 1, col + 1), false)
        }

        else new Mover(false, "", false)

    }
  }
}