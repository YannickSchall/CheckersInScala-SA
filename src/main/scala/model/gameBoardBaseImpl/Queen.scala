package model.gameBoardBaseImpl

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import model.gameBoardBaseImpl.*
import model.gameBoardBaseImpl.Color.{Black, White}
import utils.Mover
import model.gameBoardBaseImpl.Direction.{DownLeft, DownRight, Left, Right, UpLeft, UpRight}

import scala.concurrent.Future


case class Queen(state: String = "queen", row: Int, col: Int, getColor: Color) extends Piece(state, row, col, getColor) {

  //var sList: ListBuffer[String] = ListBuffer()
  var sList: ListBuffer[String] = ListBuffer()
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
    def help_bool(row_dist: Int, col_dist: Int): Boolean = (row + row_dist != 0 && row + row_dist != Last && col + col_dist != Last && col + col_dist != 0) && gameBoard.field(row + row_dist, col + col_dist).piece.isDefined && gameBoard.field(row + row_dist, col + col_dist).piece.get.getColor == (if (getColor == Black) White else Black) && gameBoard.field(row + (if row_dist < 0 then row_dist-1 else row_dist+1), col + (if col_dist < 0 then col_dist-1 else col_dist+1)).piece.isEmpty

    (getColor, direction) match {
      case (_, UpLeft|UpRight|DownLeft|DownRight) => help_bool(row_dist, col_dist)
      case _ => false
    }
  }

  override def fillList(to: String, gameBoard: GameBoard, direction: Direction, dist_count: Int): ListBuffer[String] = {

    val Last: Int = gameBoard.size - 1
    val row_dist: Int = dist_count * direction.dir._1
    val col_dist: Int = dist_count * direction.dir._2
    val row_dist2: Int = row_dist + (if row_dist>0 then 1 else -1)
    val col_dist2: Int = col_dist + (if col_dist>0 then 1 else -1)

    if (!((col + col_dist < Last) && (col + col_dist > 0) && (row + row_dist < Last) && (row + row_dist > 0))) return sList

    if (!(gameBoard.field(row + row_dist, col + col_dist).piece.isEmpty || (dist_count == 0))) {
      if (capturable(to, direction, row_dist, col_dist, gameBoard))
        return sList += gameBoard.field(row, col).pos + " " + gameBoard.field(row + row_dist2, col + col_dist2).pos
    }
    fillList(to, gameBoard, direction, dist_count+1)
  }

  // funktion höheren ordnungs // currying
  private def calcDist(direction: Direction): Int => (Int, Int) = dist_count => {
    val row_dist: Int = dist_count * direction.dir._1
    val col_dist: Int = dist_count * direction.dir._2
    //println("result: "+(row_dist, col_dist))
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


  override def getMover(to: String, gameBoard: GameBoard): Future[Mover] = {
    val Last: Int = gameBoard.size - 1
    val toRow: Int = Integer.parseInt(to.tail) - 1
    val toCol: Int = to.charAt(0).toInt - 65
    val direction: Direction = getDirection(toRow, toCol)
    var fin: Boolean = false


    @tailrec
    def getLegalMoves(ls: List[String]): List[String] = { // Füllt Liste mit legalen Spielzügen
      val dist: Int = if (ls.isEmpty) 1 else ls.size+1
      val vector = calcDist(direction)(dist) // Richtungsvektor aus direction und dist
      val row_vector: Int = vector(0)
      val col_vector: Int = vector(1)
      // Guard Statement Schleifenabbruchbedingung: Rand des Spielfelds erreicht
      if(!((col + col_vector <= Last) && (col + col_vector >= 0) &&
        (row + row_vector <= Last) && (row + row_vector >= 0))) {
        return ls
      }

      val curr_field: Field = gameBoard.field(row + row_vector, col + col_vector)
      if (curr_field.piece.isEmpty) {
        getLegalMoves(curr_field.pos :: ls)
      } else {
        ls
      }

    }


    @tailrec
    def getLegalCaps(dist: Int = 1): Option[String] = { // Gibt legalen Schlag aus
      val vector = calcDist(direction)(dist) // Richtungsvektor aus direction und dist
      val row_vector: Int = vector(0)
      val col_vector: Int = vector(1)
      // Guard Statement Schleifenabbruchbedingung: Rand des Spielfelds erreicht
      if (!((col + col_vector <= Last) && (col + col_vector >= 0) &&
        (row + row_vector <= Last) && (row + row_vector >= 0))) {
        return None
      }

      val curr_field: Field = gameBoard.field(row + row_vector, col + col_vector)
      if (curr_field.piece.isEmpty) {
        if (fin) Some(curr_field.pos) else getLegalCaps(dist+1)
      } else if (cap_cond(row_vector, col_vector, gameBoard)) {
        fin = true
        getLegalCaps(dist+1)
      } else {
        None
      }

    }


    val can_capture = sList.nonEmpty
    if (!can_capture) {
      Future.successful{
        val legals = getLegalMoves(List[String]())
        direction match {
          case UpLeft | DownLeft if col == 0 => new Mover(false, "", false)
          case UpRight | DownRight if col == Last => new Mover(false, "", false)
          case UpLeft | UpRight if row == 0 => new Mover(false, "", false)
          case DownLeft | DownRight if row == Last => new Mover(false, "", false)
          case UpLeft if legals.contains(to) => Mover(true, "", false)
          case UpRight if legals.contains(to) => Mover(true, "", false)
          case DownLeft if legals.contains(to) => Mover(true, "", false)
          case DownRight if legals.contains(to) => Mover(true, "", false)
          case _ => new Mover(false, "", false)
        }
      }
    } else {
      Future.successful {
        val legal = getLegalCaps()
        direction match {
          case UpLeft | DownLeft if col == 0 || col == 1 => new Mover(false, "", false)
          case UpRight | DownRight if col == Last || col == Last - 1 => new Mover(false, "", false)
          case UpLeft | UpRight if row == 0 || col == 1 => new Mover(false, "", false)
          case DownLeft | DownRight if row == Last || row == Last - 1 => new Mover(false, "", false)
          case UpLeft | UpRight | DownLeft | DownRight if legal.isDefined => Mover(true, legal.get, false)
          case _ => new Mover(false, "", false)
        }
      }
    }
  }

  override def movePossible(to: String, gameBoard: GameBoard): Future[Mover] = {
    val dist: Int = 1
    val Last: Int = gameBoard.size - 1

    col match {
      case 0 =>
        if (sList.isEmpty) {
          fillList(to, gameBoard, UpRight, dist)
          fillList(to, gameBoard, DownRight, dist)
        }
        getMover(to, gameBoard)

      case Last =>
        if (sList.isEmpty) {
          fillList(to, gameBoard, UpLeft, dist)
          fillList(to, gameBoard, DownLeft, dist)
        }
        getMover(to, gameBoard)

      case _ =>
        if (sList.isEmpty) {
          fillList(to, gameBoard, UpRight, dist)
          fillList(to, gameBoard, DownRight, dist)
          fillList(to, gameBoard, UpLeft, dist)
          fillList(to, gameBoard, DownLeft, dist)
        }
        getMover(to, gameBoard)
    }
  }
}