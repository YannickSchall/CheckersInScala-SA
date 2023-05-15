package fileIOComponent.dbImpl.Slick

import com.google.inject.Inject
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.model.gameBoardBaseImpl.{Color, Piece}
import fileIOComponent.model.GameBoardInterface
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import slick.jdbc.JdbcBackend.Database

import scala.util.control.Breaks.break

import model.gameBoardBaseImpl.fileIOJsonImpl.FileIO

class SlickDBCheckers @Inject () extends DBInterface {
  val connectIP = sys.env.getOrElse("POSTGRES_IP", "localhost").toString
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 5432).toString.toInt
  val db_user = sys.env.getOrElse("POSTGRES_USER", "postgres").toString
  val db_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres").toString
  val db_name = sys.env.getOrElse("POSTGRES_DB", "postgres").toString

  val database =
    Database.forURL(
      url = "jdbc:postgresql://" + connectIP + ":" + connectPort + "/" + db_name + "?serverTimezone=UTC",
      user = db_user,
      password = db_pw,
      driver = "org.postgresql.Driver")

  val gameBoardTable = new TableQuery(new GameBoardTable(_))

/*
  override def createDB(): Unit =
    val gameBoardDB = Future(Await.result(database.run(gameBoardTable.schema.createIfNotExists), Duration.Inf))
    gameBoardDB.onComplete {
      case Success(_) => print("Connection to DB & Creation of gameBoard successful!")
      case Failure(e) => print("Error: " + e)
    }

  override def readPiece(row: Int, col: Int): Option[(String, Int, Int, Color)] =
    val actionQuery = sql"""SELECT * FROM "GAMEBOARD" WHERE "row" = $row AND "column" = $col""".as[(String, Int, Int, Color)]
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    result match {
      case Seq(a) => Some(a) // Piece (row, col, state, color)
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



  override def createGameboard(size: Int): Unit =
      if isGBcreated() then
        dropGB()
        return println("")// GB already exists

      Try({
        var counter = 1
        (0 to size).flatMap(col =>
          (0 to size).reverse.map(row => {
            database.run(gameBoardTable += (counter, row, col, None)) // ?? TODO: What should be in GBTable
            counter + 1
          }))
      }) match {
        case Success(_) =>
          println("42 Grid Felder wurden erstellt");
        case Failure(exception) => println(exception);
      }

  def isGBcreated(): Boolean =
    val actionQuery = sql"""SELECT * FROM "GAMEBOARD"""".as[(Int, Int, Int, Option[Piece])] // TODO: as what ?
    val result = Await.result(database.run(actionQuery), atMost = 10.second)
    //!result.toList.isEmpty

  override def dropGB() =
    val action = gameBoardTable.delete
    Await.result(database.run(action), atMost = 10.second)
    val deleteQuery = sql"""ALTER SEQUENCE "GAMEBOARD_id_seq" RESTART WITH 1""".as[Int]
    Await.result(database.run(deleteQuery), atMost = 10.second)

  override def resetGB() =
    val new_value = "None"
    val actionQuery = sql"""UPDATE "GAMEBOARD" SET "value" = $new_value WHERE "value" != $new_value""".as[Int]
    Await.result(database.run(actionQuery), atMost = 10.second)
*/

  def save(gameBoard: GameBoardInterface): Unit = {
    Try {
      println("saving game in DB")
      val jsonGb = fileIO.gameBoardToJson()
      val gameState = (jsonGb \ "gameBoard").get.toString()
    }
  }

  def load(id: Option[Int] = None): Try[GameBoardInterface] =
    Try {
      val loadQuery = id.map(id => gameBoardTable.filter(_.id === id))
        .getOrElse(gameBoardTable.filter(_.id === gameBoardTable.map(_.id).max))

      val gameBoard =  Await.result(database.run(loadQuery.result), 5.seconds)
      val gb =

    }

}
