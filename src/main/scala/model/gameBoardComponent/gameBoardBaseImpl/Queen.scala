package model.gameBoardComponent.gameBoardBaseImpl

import util.Mover

import scala.collection.mutable.ListBuffer

case class Queen(state: String = "queen", row: Int, col: Int, getColor: String) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()
  override def toString: String = if (getColor == "black") "\u001B[37mQ\u001B[0m"//"\uD83D\uDFE0" //orange/black
  else "\u001B[30mQ\u001B[0m"//"\uD83D\uDFE3" //purple/white

  /*
  override def whiteMovePossible(to: String, gameBoard: GameBoard): Mover = {
    var x = 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>

        x = 0
        // def m(col, row, peice, x) : while (col && row) && piece || x
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
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
        if (toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen linker rand nach rechts oben
        x = 0
        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen linker rand nach rechts unten

        else new Mover(false, "", false)


      case Last =>

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != Last && row+x != Last && col-x != 0 && col != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty) {
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
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == "black" && gameBoard.field(row - (x-1), col - (x-1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen rechter rand nach links oben

        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == "black" && gameBoard.field(row + (x-1), col - (x-1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen rechter rand nach links unten

        else new Mover(false, "", false)


      case _ =>

        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != 0 && col-x != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == "black" && gameBoard.field(row + (x+1) , col - (x+1)).piece.isEmpty) {
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
        if (toCol - col == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen mitte nach rechts oben

        x = 0
        while ((col + x <= Last) && (row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen mitte nach rechts unten

        x = 0
        while ((col - x >= 0) && (row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == "black" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen mitte nach links oben

        x = 0
        while ((col - x >= 0) && (row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == "black" && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen mitte nach links unten
        else new Mover(false, "", false)
      }
    }
*/

// ok


  override def capturable(to: String, dist: Int, gameBoard: GameBoard): Boolean = {
    val Last: Int = gameBoard.size - 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65

    def direction: String = {
      var str: String = ""
      str = if (toRow < row) "up" else "down"
      str = if (toCol < col) str+"_left" else str+"_right"
      str
    }
  //def help_bool(dist: Int): Boolean = (row != 0 && row + dist != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty
    def help_bool(dist: Int): Boolean = (row != 0 && row + dist != 0 && col != Last && col + dist != Last) && gameBoard.field(row + dist, col + dist).piece.isDefined && gameBoard.field(row + dist, col + dist).piece.get.getColor == (if (getColor == "black") "white" else "black") && gameBoard.field(row + (dist + 1), col + (dist + 1)).piece.isEmpty

    (getColor, direction) match {
      case ("white", _) if row == 1 || row == 0 => false
      case ("black", _) if row == Last || row == Last - 1 => false
      case ("white", "left") => help_bool(1)
      case ("white", "right") => help_bool(-1)
      case ("black", "left") => help_bool(1)
      case ("black", "right") => help_bool(1)
    }
    return false
  }

  override def fillList(to: String, gameBoard: GameBoard, dist: Int): ListBuffer[String] = {
    // brauchen wir 2 unterschiedliche Listen? früher blackList
    val Last: Int = gameBoard.size - 1

    if (!((col + dist < Last && row + dist > 0) && gameBoard.field(row + dist, col + dist).piece.isEmpty || (dist == 0))) {
      if (capturable(to, dist, gameBoard))
        sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (dist + 1), col + (dist + 1)).pos
    }
    fillList(to, gameBoard, dist+1)

  }



  override def getMover(to: String, gameBoard: GameBoard): Mover = ???
  override def movePossible(to: String, gameBoard: GameBoard): Mover = {


    var x = 1
    val dist: Int = 0
    var toRow: Int = Integer.parseInt(to.tail) - 1
    var toCol: Int = to.charAt(0).toInt - 65
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>

        fillList("", gameBoard, dist)



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
        if (toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen linker rand nach rechts oben
        x = 0
        while ((col + x <= Last && row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == "white" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen linker rand nach rechts unten

        else new Mover(false, "", false)


      case Last =>

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if ((row != Last && row+x != Last && col-x != 0 && col != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == "white" && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty) {
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
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == "white" && gameBoard.field(row - (x-1), col - (x-1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen rechter rand nach links oben

        x = 0
        while ((col - x >= 0 && row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == "white" && gameBoard.field(row + (x-1), col - (x-1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen rechter rand nach links unten

        else new Mover(false, "", false)


      case _ =>
        //
        x = 0
        while ((col + x < Last && row - x > 0) && gameBoard.field(row - x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
          print("\nx: "+x+"\n")
        }
        if ((row != 0 && row-x != 0 && col != Last && col+x != Last) && gameBoard.field(row - x, col + x).piece.isDefined && gameBoard.field(row - x, col + x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty) {
          // fill list
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col + x < Last && row + x < Last) && gameBoard.field(row + x, col + x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != Last && col+x != Last) && gameBoard.field(row + x, col + x).piece.isDefined && gameBoard.field(row + x, col + x).piece.get.getColor == "white" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col + (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row - x > 0) && gameBoard.field(row - x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != 0 && row-x != 0 && col != 0 && col-x != 0) && gameBoard.field(row - x, col - x).piece.isDefined && gameBoard.field(row - x, col - x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row - (x + 1), col - (x + 1)).pos
        }

        x = 0
        while ((col - x > 0 && row + x < Last) && gameBoard.field(row + x, col - x).piece.isEmpty || (x == 0)) {
          x += 1
        }
        if ((row != Last && row+x != Last && col != 0 && col-x != 0) && gameBoard.field(row + x, col - x).piece.isDefined && gameBoard.field(row + x, col - x).piece.get.getColor == "white" && gameBoard.field(row + (x+1) , col - (x+1)).piece.isEmpty) {
          sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + (x + 1), col - (x + 1)).pos
        }

        // getMover()
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


        if (toCol - col == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col + x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col + (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col + x), false) // schlagen mitte nach rechts oben

        x = 0
        while ((col + x <= Last) && (row + x <= Last) && gameBoard.field(row + x, col + x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (toCol - col == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col + x).piece.get.getColor == "white" && gameBoard.field(row + (x+1), col + (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col + x), false) // schlagen mitte nach rechts unten

        x = 0
        while ((col - x >= 0) && (row - x >= 0) && gameBoard.field(row - x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && row-toRow == x+1 && gameBoard.field(row - x, col - x).piece.get.getColor == "white" && gameBoard.field(row - (x+1), col - (x+1)).piece.isEmpty && row - x > 0) return new Mover(true, posToStr(row - x, col - x), false) // schlagen mitte nach links oben

        x = 0
        while ((col - x >= 0) && (row + x <= Last) && gameBoard.field(row + x, col - x).piece.isEmpty || x == 0) {
          x += 1
        }
        if (col - toCol == x+1 && toRow-row == x+1 && gameBoard.field(row + x, col - x).piece.get.getColor == "white" && gameBoard.field(row + (x+1), col - (x+1)).piece.isEmpty && row + x < Last) return new Mover(true, posToStr(row + x, col - x), false) // schlagen mitte nach links unten
        else new Mover(false, "", false)
      }
    }

}

/*

def get_possible_moves(position, board):
    row, col = position
    player = board[row][col]
    opponent = 2 if player == 1 else 1  # determine opponent's stone color

    # define helper function to check if a square is on the board and empty
    def is_valid_square(r, c):
        return 0 <= r < 8 and 0 <= c < 8 and board[r][c] == 0

    # define helper function to check if a square is occupied by an opponent's stone
    def is_opponent_square(r, c):
        return 0 <= r < 8 and 0 <= c < 8 and board[r][c] == opponent

    # define helper function to recursively check for possible moves/captures in a direction
    def check_direction(dr, dc, moves, captures):
        r, c = row + dr, col + dc
        if is_valid_square(r, c):
            moves.append((r, c))
            if not captures:
                # if no captures have been made yet, continue checking in this direction
                check_direction(dr, dc, moves, captures)
        elif is_opponent_square(r, c):
            r, c = r + dr, c + dc
            if is_valid_square(r, c):
                captures.append((r, c))
                check_direction(dr, dc, moves, captures)

    # check all 4 diagonal directions for moves/captures
    moves = []
    captures = []
    for dr, dc in [(1, 1), (1, -1), (-1, 1), (-1, -1)]:
        check_direction(dr, dc, moves, captures)

    return captures if captures else moves


*/