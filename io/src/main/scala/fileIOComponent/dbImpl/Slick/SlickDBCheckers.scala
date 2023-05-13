package fileIOComponent.dbImpl.Slick

import fileIOComponent.dbImpl.DBInterface

import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

class SlickDBCheckers @Inject () extends DBInterface {
  val connectIP = sys.env.getOrElse("POSTGRES_IP", "localhost").toString
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 5432).toString.toInt
  val db_user = sys.env.getOrElse("POSTGRES_USER", "postgres").toString
  val db_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres").toString
  val db_name = sys.env.getOrElse("POSTGRES_DB", "postgres").toString


  val playerTable = TableQuery[PlayerTable]
  val gameBoardTable = TableQuery[GabeBoardTable]


  override def createDB(): Unit =
    val playerDB = Future(Await.result(database.run(playerTable.schema.createIfNotExists), Duration.Inf))
    val gameBoardDB = Future(Await.result(database.run(gridTable.schema.createIfNotExists), Duration.Inf))
    playerDB.onComplete {
      case Success(_) => print("Connection to DB & Creation of playerTable successful!")
      case Failure(e) => print("Error: " + e)
    }
    gridDB.onComplete {
      case Success(_) => print("Connection to DB & Creation of gameBoard successful!")
      case Failure(e) => print("Error: " + e)
    }

  override def readPiece(row: Int, col: Int): Option[(String, Int, Int, Color)] =
    val actionQuery = sql"""SELECT * FROM "GAMEBOARD" WHERE "row" = $row AND "column" = $col""".as[(String, Int, Int, Color)]
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    result match {
      case Seq(a) => Some((a._1, a._2, a._3, a._4))
      case _ => None
    }

  override def updatePiece(row: Int, col: Int, value: String): String =
    if readPiece(row, col) == None then
      return "Update Failed - Piece wasn't set on Gameboard."
      // würde set rausnehmen
    val actionQuery =
      sql"""UPDATE "GAMEBOARD" SET "value" = $value WHERE "row" = $row AND "column" = $col""".as[(Int, Int, String)]
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    result.toString()


  override def createPlayer(color: Color): Color =
    if readAllPlayers().length = 2 then
      return -1

    Try({
      if readAllPlayers().contains(color.White) then
        db.run(playerTable += (color.Black))
        color.Black
      else if  readAllPlayers().contains(color.White) then
        db.run(playerTable += (color.Black))
        color.Black
    }) match {
      case Success(_) =>
        println("Player successuflly added "); player.playerNumber
      case Failure(exception) => println(exception); -1
    }


  override def createGameboard(size: Int) =
      if isGBcreated() then
        resetGrid()
        break;// GB already exists

    Try({
        var counter = 1
        (0 to size).flatMap(col =>
          (0 to size).reverse.map(row => {
            db.run(gameBoardTable += (counter, row, col, "None")) // ?? TODO: What should be in GBTable
            counter + 1
          }))
      }) match {
        case Success(_) =>
          println("42 Grid Felder wurden erstellt");
        case Failure(exception) => println(exception);
      }

  def isGBcreated(): Boolean =
    val actionQuery = sql"""SELECT * FROM "GAMEBOARD"""".as[(Int, Int, Int, String)] // TODO: as what ?
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    !result.toList.isEmpty

  override def readAllPlayers(): List[(String, Int, Int, Color)] = // Liste aus einem Piece
    val actionQuery = sql"""SELECT * FROM "PLAYER"""".as[(String, Int, Int, Color)]
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    result.toList

  override def dropPlayers() =
    val action = db.delete
    Await.result(database.run(action), atMost = 10.second)
    val deleteQuery = sql"""ALTER SEQUENCE "PLAYER_id_seq" RESTART WITH 1""".as[Int]
    Await.result(database.run(deleteQuery), atMost = 10.second)

  override def dropGB() =
    val action = gameBoardTable.delete
    Await.result(database.run(action), atMost = 10.second)
    val deleteQuery = sql"""ALTER SEQUENCE "GAMEBOARD_id_seq" RESTART WITH 1""".as[Int]
    Await.result(database.run(deleteQuery), atMost = 10.second)

  override def resetGB() =
    val new_value = "None"
    val actionQuery = sql"""UPDATE "GAMEBOARD" SET "value" = $new_value WHERE "value" != $new_value""".as[Int]
    Await.result(database.run(actionQuery), atMost = 10.second)

  override def readGrid(): GridInterface =
    ???

}
