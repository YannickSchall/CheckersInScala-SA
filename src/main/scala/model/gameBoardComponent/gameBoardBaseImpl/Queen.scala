package model.gameBoardComponent.gameBoardBaseImpl

import util.Mover

import scala.collection.mutable.ListBuffer
import model.gameBoardComponent.gameBoardBaseImpl.Color.*
case class Queen(state: String = "queen", row: Int, col: Int, getColor: Color) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()
  override def toString: String = if (getColor == Black) "\u001B[37mQ\u001B[0m"//"\uD83D\uDFE0" //orange/black
  else "\u001B[30mQ\u001B[0m"//"\uD83D\uDFE3" //purple/white

  override def whiteMovePossible(to: String, gameBoard: GameBoard): Mover = {
    var x = 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>

        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        if (sList.isEmpty) {
        x = 0

        while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((toCol - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((toCol - col) - (row - toRow) == 0) && (toCol - col > 0) && (toRow - row < 0)) return new Mover(true, "", false) // linker rand nach rechts oben

        x = 0

        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((toCol - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((toCol - col) - (toRow - row) == 0) && (toCol - col > 0) && (toRow - row > 0)) return new Mover(true, "", false) // linker rand nach rechts unten
        }

        x = 0
        while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen linker rand nach rechts oben
        x = 0
        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen linker rand nach rechts unten

        else new Mover(false, "", false)


      case Last =>

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != Last && row+x != Last && col-x != 0 && col != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col - (x + 1)).pos
        }

        if (sList.isEmpty) {
        x = 0
        while ((col - x >= 0 && row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((col - toCol) - (row - toRow) == 0) && (toCol - col < 0) && (toRow - row < 0)) return new Mover(true, "", false) // rechter rand nach links oben

        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((col - toCol) - (toRow - row) == 0) && (toCol - col < 0) && (toRow - row > 0)) return new Mover(true, "", false) // rechter rand nach links unten
        }

        x = 0
        while ((col - x >= 0 && row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == Black && gameBoard.field(row - (x-1), col - (x-1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen rechter rand nach links oben

        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == Black && gameBoard.field(row + (x-1), col - (x-1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen rechter rand nach links unten

        else new Mover(false, "", false)


      case _ =>

        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != 0 && col-x != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == Black && gameBoard.field(row + (x+1) , col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col - (x + 1)).pos
        }

        if (sList.isEmpty) {
        x = 0
        while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) { //CHECKED
          x += 1
        }
        if (((toCol - col) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((toCol - col) - (row - toRow) == 0) && (toCol - col > 0) && (toRow - row < 0)) return new Mover(true, "", false) //mitte nach rechts oben

        x = 0
        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if (((toCol - col) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((toCol - col) - (toRow - row) == 0) && (toCol - col > 0) && (toRow - row > 0)) return new Mover(true, "", false) //mitte nach rechts unten

        x = 0
        while ((col - x >= 0 && row - x >= 0) && (gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0))) {
          x += 1
        }
        if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((col - toCol) - (row - toRow) == 0) && (toCol - col < 0) && (toRow - row < 0)) return new Mover(true, "", false) //mitte nach links oben


        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((col - toCol) - (toRow - row) == 0) && (toCol - col < 0) && (toRow - row > 0)) return new Mover(true, "", false) //mitte nach links unten

        }

        x = 0
        while ((col + x <= Last) && (row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen mitte nach rechts oben

        x = 0
        while ((col + x <= Last) && (row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen mitte nach rechts unten

        x = 0
        while ((col - x >= 0) && (row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == Black && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen mitte nach links oben

        x = 0
        while ((col - x >= 0) && (row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == Black && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen mitte nach links unten
        else new Mover(false, "", false)
      }
    }


  override def blackMovePossible(to: String, gameBoard: GameBoard): Mover = {

    var x = 1
    var toRow: Int = Integer.parseInt(to.tail) - 1
    var toCol: Int = to.charAt(0).toInt - 65
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>

        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == White && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == White && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        if (sListBlack.isEmpty) {
          x = 0

          while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
            x += 1
          }
          if ((toCol - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((toCol - col) - (row - toRow) == 0) && (toCol - col > 0) && (toRow - row < 0)) return new Mover(true, "", false) // linker rand nach rechts oben

          x = 0

          while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
            x += 1
          }
          if ((toCol - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((toCol - col) - (toRow - row) == 0) && (toCol - col > 0) && (toRow - row > 0)) return new Mover(true, "", false) // linker rand nach rechts unten
        }

        x = 0
        while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == White && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen linker rand nach rechts oben
        x = 0
        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == White  && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen linker rand nach rechts unten

        else new Mover(false, "", false)


      case Last =>

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == White && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != Last && row+x != Last && col-x != 0 && col != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == White && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col - (x + 1)).pos
        }

        if (sListBlack.isEmpty) {
          x = 0
          while ((col - x >= 0 && row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
            x += 1
          }
          if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((col - toCol) - (row - toRow) == 0) && (toCol - col < 0) && (toRow - row < 0)) return new Mover(true, "", false) // rechter rand nach links oben

          x = 0
          while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
            x += 1
          }
          if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((col - toCol) - (toRow - row) == 0) && (toCol - col < 0) && (toRow - row > 0)) return new Mover(true, "", false) // rechter rand nach links unten
        }

        x = 0
        while ((col - x >= 0 && row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == White && gameBoard.field(row - (x-1), col - (x-1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen rechter rand nach links oben

        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == White && gameBoard.field(row + (x-1), col - (x-1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen rechter rand nach links unten

        else new Mover(false, "", false)


      case _ =>

        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
          print("\nx: "+x+"\n")
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == White && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == White && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == White && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != 0 && col-x != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == White && gameBoard.field(row + (x+1) , col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col - (x + 1)).pos
        }

        if (sListBlack.isEmpty) {
          x = 0
          while ((col + x <= Last && row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) { //CHECKED
            x += 1
          }
          if (((toCol - col) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((toCol - col) - (row - toRow) == 0) && (toCol - col > 0) && (toRow - row < 0)) return new Mover(true, "", false) //mitte nach rechts oben

          x = 0
          while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
            x += 1
          }
          if (((toCol - col) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((toCol - col) - (toRow - row) == 0) && (toCol - col > 0) && (toRow - row > 0)) return new Mover(true, "", false) //mitte nach rechts unten

          x = 0
          while ((col - x >= 0 && row - x >= 0) && (gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0))) {
            x += 1
          }
          if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((col - toCol) - (row - toRow) == 0) && (toCol - col < 0) && (toRow - row < 0)) return new Mover(true, "", false) //mitte nach links oben

          x = 0
          while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
            x += 1
          }
          if (((col - toCol) - (x - 1) <= 0) && ((x - 1) >= (toRow - row)) && ((col - toCol) - (toRow - row) == 0) && (toCol - col < 0) && (toRow - row > 0)) return new Mover(true, "", false) //mitte nach links unten

        }

        x = 0
        while ((col + x <= Last) && (row - x >= 0) && gameBoard.field(row - x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == White && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen mitte nach rechts oben

        x = 0
        while ((col + x <= Last) && (row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == White && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen mitte nach rechts unten

        x = 0
        while ((col - x >= 0) && (row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == White && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen mitte nach links oben

        x = 0
        while ((col - x >= 0) && (row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == White && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen mitte nach links unten
        else new Mover(false, "", false)
      }
    }

}