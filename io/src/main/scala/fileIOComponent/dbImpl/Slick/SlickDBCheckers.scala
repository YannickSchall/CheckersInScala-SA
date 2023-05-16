package fileIOComponent.dbImpl.Slick

import com.google.inject.Inject
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.{Color, GameBoard, Piece}
import play.api.libs.json.JsPath.\
import play.api.libs.json.JsValue
import play.api.libs.json.Json.parse
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import java.io.PrintWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.control.Breaks.break
import scala.util.{Failure, Success, Try}


class SlickDBCheckers @Inject() extends DBInterface {
  val databaseDB: String = sys.env.getOrElse("MYSQL_DATABASE", "checkers")
  val databaseUser: String = sys.env.getOrElse("MYSQL_USER", "user")
  val databasePassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "root")
  val databasePort: String = sys.env.getOrElse("MYSQL_PORT", "3306")
  val databaseHost: String = sys.env.getOrElse("MYSQL_HOST", "localhost")
  val databaseUrl = s"jdbc:mysql://$databaseHost:$databasePort/$databaseDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true"

  println(databaseUrl)
  val database = Database.forURL(
    url = databaseUrl,
    driver = "com.mysql.cj.jdbc.Driver",
    user = databaseUser,
    password = databasePassword
  )

  val io = new IO()
  val gameBoardTable = new TableQuery(new GameBoardTable(_))

  override def save(gameBoard: GameBoardInterface): Unit = {
    Try {
      println("saving game in DB")
      val jsonGb = parse(io.gameBoardToJson(gameBoard))
      val gbFromJson = (jsonGb \ "gameBoard").get.toString()
      val gb = (0, gbFromJson)
      val test = (1, "test")
      Await.result(database.run(gameBoardTable returning gameBoardTable.map(_.id) += test), 15.seconds)
    }
    val jsonGb = parse(io.gameBoardToJson(gameBoard))
    val gbFromJson = (jsonGb \ "gameBoard").get.toString()
    new PrintWriter("savefile") {
      write(gbFromJson);
      close
    }
  }

  override def load(id: Option[Int] = None): Try[GameBoardInterface] = {
    Try {
      println("storing game in DB")
      val loadQuery = id.map(id => gameBoardTable.filter(_.id === id))
        .getOrElse(gameBoardTable.filter(_.id === gameBoardTable.map(_.id).max))

      val answer = Await.result(database.run(loadQuery.result), 5.seconds)
      val slave = new GameBoard(8)
      val res = try {
        slave.jsonToGameBoard(answer.head(1))
      } catch {
        case e: Exception =>
          e.printStackTrace()
          throw e
      }
      res
    }
  }

  override def update(id: Int, gamestate: String): Unit = {
    Try {
      val gamestateQuery = gameBoardTable.filter(_.id === id).map(_.gamestate).update(gamestate)
      val query = gamestateQuery
      Await.result(database.run(query), 5.seconds)
      true
    }
  }

  override def deleteGame(id: Int): Try[Boolean] = {
    Try {
      Await.result(database.run(gameBoardTable.filter(_.id === id).delete), 5.seconds)
      true
    }
  }

  def sanitize(str: String): String = {
    str.replace("\\n", "\n")
      .replace("\\r", "\r")
      .replace("\\t", "\t")
      .replace("\\b", "\b")
      .replace("\\f", "\f")
      .replace("\\\\", "\\")
      .replace("\\\"", "\"")
      .replace("\\'", "'")
      .replace("\"\"", "\"")
  }
}



