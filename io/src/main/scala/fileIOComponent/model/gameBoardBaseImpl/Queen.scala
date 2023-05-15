package fileIOComponent.model.gameBoardBaseImpl

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import fileIOComponent.model.gameBoardBaseImpl.Color.{Black, White}
import fileIOComponent.model.gameBoardBaseImpl.Direction.*
import fileIOComponent.utils.Mover

case class Queen(state: String = "queen", row: Int, col: Int, getColor: Color) extends Piece(state, row, col, getColor) {

  var sList: ListBuffer[String] = ListBuffer()
  var sListBlack: ListBuffer[String] = ListBuffer()
  override def toString: String = if (getColor == Black) "\u001B[37mQ\u001B[0m"//"\uD83D\uDFE0" //orange/black
  else "\u001B[30mQ\u001B[0m"//"\uD83D\uDFE3" //purple/white


  // ok
  override def cap_cond(row_offset: Int, col_offset: Int, gameBoard: GameBoard): Boolean = gameBoard.field(row + row_offset, col + col_offset).piece.isDefined && gameBoard.field(row + row_offset, col + col_offset).piece.get.getColor == (if (getColor == Black) White else Black)

  override def getDirection(toRow: Int, toCol: Int): Direction = {
    (toRow < row, toCol < col) match {
      case (true, true) => UpLeft
      case (true, false) => UpRight
      case (false, true) => DownLeft
      case (false, false) => DownRight
    }
  }

  override def capturable(to: String, direction: Direction, row_dist: Int, col_dist: Int, gameBoard: GameBoard): Boolean = {
    val Last: Int = gameBoard.size - 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val direction: Direction = getDirection(toRow, toCol)


    def help_bool(row_dist: Int, col_dist: Int): Boolean = (row + row_dist != 0 && row + row_dist != Last && col + col_dist != Last && col + col_dist != 0) && gameBoard.field(row + row_dist, col + col_dist).piece.isDefined && gameBoard.field(row + row_dist, col + col_dist).piece.get.getColor == (if (getColor == Black) White else Black) && gameBoard.field(row + (if row_dist < 0 then row_dist-1 else row_dist+1), col + (if col_dist < 0 then col_dist-1 else col_dist+1)).piece.isEmpty

    (getColor, direction) match {
      case (_, UpLeft) => help_bool(-row_dist, -col_dist)
      case (_, UpRight) => help_bool(-row_dist, col_dist)
      case (_, DownLeft) => help_bool(row_dist, -col_dist)
      case (_, DownRight) => help_bool(row_dist, col_dist)
      case _ => false
    }
  }

  override def fillList(to: String, gameBoard: GameBoard, direction: Direction, dist_count: Int): ListBuffer[String] = {
    // brauchen wir 2 unterschiedliche Listen? früher blackList
    val Last: Int = gameBoard.size - 1
    val row_dist: Int = dist_count * direction.dir._1
    val col_dist: Int = dist_count * direction.dir._2
    val row_dist2: Int = row_dist + (if row_dist>0 then 1 else -1)
    val col_dist2: Int = col_dist + (if col_dist>0 then 1 else -1)
    
    if (!((col + col_dist < Last && row + row_dist > 0) && gameBoard.field(row + row_dist, col + col_dist).piece.isEmpty || (dist_count == 0))) {
      if (capturable(to, direction, row_dist, col_dist, gameBoard))
        sListBlack += gameBoard.field(row, col).pos + " " + gameBoard.field(row + row_dist2, col + col_dist2).pos
    }
    fillList(to, gameBoard, direction, dist_count+1)
  }

  // funktion höheren ordnungs // currying
  private def calcDist(direction: Direction): Int => (Int, Int) = dist_count => {
    val row_dist: Int = dist_count * direction.dir._1
    val col_dist: Int = dist_count * direction.dir._2
    (row_dist, col_dist)
  }

  // pattern matching
  private def increaseOffset(offset: Int): Int = offset match {
    case x if x > 0 => x + 1
    case x if x < 0 => x - 1
    case _ => 0
  }

  // funktionale Komposition
  private def calcNextDist(direction: Direction): Int => (Int, Int) =
    calcDist(direction) andThen { case (rd, cd) => (rd + increaseOffset(rd), cd + increaseOffset(cd)) }


  override def getMover(to: String, gameBoard: GameBoard): Mover = {
    val Last: Int = gameBoard.size - 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val direction: Direction = getDirection(toRow, toCol)


    @tailrec
    def getDist(dist_count: Int): Int = {
      val row_dist: Int = calcDist(direction)(dist_count)(0)
      val col_dist: Int = calcDist(direction)(dist_count)(1)
      if (!((col + col_dist < Last && row + row_dist > 0) && gameBoard.field(row + row_dist, col + col_dist).piece.isEmpty || (dist_count == 0))) {
        if (capturable(to, direction, row_dist, col_dist, gameBoard))
          return dist_count
      }
      getDist(dist_count + 1)
    }



    val dist = getDist(0)
    //  if (((toCol - col) - (x - 1) <= 0) && ((x - 1) >= (row - toRow)) && ((toCol - col) - (row - toRow) == 0) && (toCol - col > 0) && (toRow - row < 0)) return new Mover(true, "", false) //mitte nach rechts oben
    val can_capture = sList.nonEmpty
    def cap(row_offset: Int, col_offset: Int): Boolean = {
      val row_offset2: Int = calcNextDist(direction)(row_offset)(0)
      val col_offset2: Int = calcNextDist(direction)(col_offset)(1)
      cap_cond(row_offset, col_offset, gameBoard) && to == gameBoard.posToStr(row + row_offset2, col + col_offset2) && gameBoard.field(row + row_offset2, col + col_offset2).piece.isEmpty}

    def no_cap(row_offset: Int, col_offset: Int): Boolean = gameBoard.field(row + row_offset, col + col_offset).piece.isEmpty && to == gameBoard.posToStr(row + row_offset, col + col_offset)

    if (!can_capture) {
      (getColor, direction) match {
        case (White, _) if row == 0 => new Mover(false, "", false)
        case (Black, _) if row == Last => new Mover(false, "", false)
        case (_, Left) if col == 0 => new Mover(false, "", false)
        case (_, Right) if col == Last => new Mover(false, "", false)
        case (White, Left) if no_cap(-dist, -dist) => new Mover(true, "", if toRow == 0 then true else false)
        case (White, Right) if no_cap(-1, 1) => new Mover(true, "", if toRow == 0 then true else false)
        case (Black, Left) if no_cap(1, -1) => new Mover(true, "", if toRow == Last then true else false)
        case (Black, Right) if no_cap(1, 1) => new Mover(true, "", if toRow == Last then true else false)
        case _ => new Mover(false, "", false)
      }
    } else {
      (getColor, direction) match {
        case (_, _) if col == 0 || col == 1 => new Mover(false, "", false)
        case (_, _) if col == Last || col == Last - 1 => new Mover(false, "", false)
        case (_, Left) if col == 0 || col == 1 => new Mover(false, "", false)
        case (_, Right) if col == Last || col == Last - 1 => new Mover(false, "", false)
        case (White, Left) if cap(-dist, -dist) => new Mover(true, posToStr(row - 1, col - 1), if toRow == 0 then true else false)
        case (White, Right) if cap(-1, 1) => new Mover(true, posToStr(row - 1, col + 1), if toRow == 0 then true else false)
        case (Black, Left) if cap(1, -1) => new Mover(true, posToStr(row + 1, col - 1), if toRow == Last then true else false)
        case (Black, Right) if cap(1, 1) => new Mover(true, posToStr(row + 1, col + 1), if toRow == Last then true else false)
        case _ => new Mover(false, "", false)
      }
    }
  }

  override def movePossible(to: String, gameBoard: GameBoard): Mover = {
    val dist: Int = 0
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>
        if (sListBlack.isEmpty) {
          fillList(to, gameBoard, UpRight, dist)
          fillList(to, gameBoard, DownRight, dist)
        }
        getMover(to, gameBoard)

      case Last =>
        if (sListBlack.isEmpty) {
          fillList(to, gameBoard, UpLeft, dist)
          fillList(to, gameBoard, DownLeft, dist)
        }
        getMover(to, gameBoard)

      case _ =>
        if (sListBlack.isEmpty) {
          fillList(to, gameBoard, UpRight, dist)
          fillList(to, gameBoard, DownRight, dist)
          fillList(to, gameBoard, UpLeft, dist)
          fillList(to, gameBoard, DownLeft, dist)
        }
        getMover(to, gameBoard)
    }
  }
}
