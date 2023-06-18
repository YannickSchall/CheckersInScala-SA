package model.gameBoardComponent.gameBoardBaseImpl

import scala.collection.mutable.ListBuffer
import util.Mover
import model.gameBoardComponent.gameBoardBaseImpl.Color.*

case class Normal(state: String = "normal", row: Int, col: Int, getColor: Color) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()

  override def toString: String = if (getColor == Black ) "\u001B[37mO\u001B[0m" //red
  else "\u001B[30mO\u001B[0m" //blue
  override def posToStr(row: Int, col: Int): String = {
    (col + 65).toChar.toString + (row + 49).toChar.toString
  }

  override def whiteMovePossible(to: String, gameBoard: GameBoard): Mover = {
    val Last: Int = gameBoard.size - 1

    col match {

      case 0 =>

        if ((row != 0 && row != 1) && gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == Black && gameBoard.field(row - 2, col + 2).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col + 2).pos
        }

        if (sList.isEmpty) {
          if (row != 0 && gameBoard.field(row - 1, col + 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row - 1, col + 1)) {
              return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == Black) {
          if ((row != 0 && row != 1) && to == gameBoard.posToStr(row - 2, col + 2) && gameBoard.field(row - 2, col + 2).piece.isEmpty) {
            return new Mover(true, posToStr(row - 1, col + 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)


      case Last =>

        if ((row != 0 && row != 1) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == Black && gameBoard.field(row - 2, col - 2).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col - 2).pos
        }

        if (sList.isEmpty) {
          if (row != 0 && gameBoard.field(row - 1, col - 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row - 1, col - 1)) {
              if (Integer.parseInt(to.tail) - 1 == 0) {
                return new Mover(true, "", true)
              } else return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == Black) {
          if ((row != 0 && row != 1) && to == gameBoard.posToStr(row - 2, col - 2) && gameBoard.field(row - 2, col - 2).piece.isEmpty) {
            return new Mover(true, posToStr(row - 1, col - 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)


      case _ =>

        if ((row != 0 && row != 1) && (col != 0 && col != 1) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == Black && gameBoard.field(row - 2, col - 2).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col - 2).pos
        }

        if ((row != 0 && row != 1) && (col != Last && col != (Last-1)) && gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == Black && gameBoard.field(row - 2, col + 2).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - 2, col + 2).pos
        }
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
        else if ((row != 0 && row != 1 && col != 1 && col != 0) && gameBoard.field(row - 1, col - 1).piece.isDefined && gameBoard.field(row - 1, col - 1).piece.get.getColor == Black && gameBoard.field(row - 2, col - 2).piece.isEmpty && to == gameBoard.posToStr(row - 2, col - 2)) {
          if (Integer.parseInt(to.tail) - 1 == 0) {
            new Mover(true, posToStr(row - 1, col - 1), true)
          } else new Mover(true, posToStr(row - 1, col - 1), false)
        }
        else if ((row != 0 && row != 1 && col != Last-1 && col != Last) && gameBoard.field(row - 1, col + 1).piece.isDefined && gameBoard.field(row - 1, col + 1).piece.get.getColor == Black && gameBoard.field(row - 2, col + 2).piece.isEmpty && to == gameBoard.posToStr(row - 2, col + 2)) {
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

        if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == White  && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
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

        else if (gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == White) {
          if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && to == gameBoard.posToStr(row + 2, col + 2) && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
            return new Mover(true, posToStr(row + 1, col + 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)



      case Last =>

        if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == White && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col - 2).pos
        }

        if (sListBlack.isEmpty) {
          if (row != Last && gameBoard.field(row + 1, col - 1).piece.isEmpty) {
            if (to == gameBoard.posToStr(row + 1, col - 1)) {
              return new Mover(true, "", false)
            } else return new Mover(false, "", false)
          } else return new Mover(false, "", false)
        }

        else if (gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == White) {
          if ((row != gameBoard.size - 1 && row != gameBoard.size - 2) && to == gameBoard.posToStr(row + 2, col - 2) && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
            return new Mover(true, posToStr(row + 1, col - 1), false)
          } else return new Mover(false, "", false)
        } else new Mover(false, "", false)


      case _ =>

        if ((col != 0 && col != 1 && row != Last && row != Last-1) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == White && gameBoard.field(row + 2, col - 2).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + 2, col - 2).pos
        }

        if ((col != Last && col != Last-1 && row != Last && row != Last-1) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == White && gameBoard.field(row + 2, col + 2).piece.isEmpty) {
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

        else if ((row != Last && row != Last-1 && col != 0 && col != 1) && gameBoard.field(row + 1, col - 1).piece.isDefined && gameBoard.field(row + 1, col - 1).piece.get.getColor == White && gameBoard.field(row + 2, col - 2).piece.isEmpty && to == gameBoard.posToStr(row + 2, col - 2)) {
          if (Integer.parseInt(to.tail) - 1 == Last) {
            new Mover(true, posToStr(row + 1, col - 1), true)
          } else new Mover(true, posToStr(row + 1, col - 1), false)
        }

        else if ((row != Last && row != Last-1 && col != Last && col != Last-1) && gameBoard.field(row + 1, col + 1).piece.isDefined && gameBoard.field(row + 1, col + 1).piece.get.getColor == White && gameBoard.field(row + 2, col + 2).piece.isEmpty && to == gameBoard.posToStr(row + 2, col + 2)) {
          if (Integer.parseInt(to.tail) - 1 == Last) {
            new Mover(true, posToStr(row + 1, col + 1), true)
          } else new Mover(true, posToStr(row + 1, col + 1), false)
        }

        else new Mover(false, "", false)

    }
  }
}