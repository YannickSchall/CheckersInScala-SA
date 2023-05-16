package fileIOComponent.dbImpl.Slick

import com.google.inject.Inject
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.gameBoardBaseImpl.{Color, GameBoard, Piece}
import fileIOComponent.model.GameBoardInterface
import play.api.libs.json.JsPath.\
import play.api.libs.json.JsValue
import slick.lifted.TableQuery
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*
import scala.util.control.Breaks.break


class SlickDBCheckers @Inject () extends DBInterface {
  val connectIP = sys.env.getOrElse("POSTGRES_IP", "localhost").toString
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 5432).toString.toInt
  val db_user = sys.env.getOrElse("POSTGRES_USER", "postgres").toString
  val db_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres").toString
  val db_name = sys.env.getOrElse("POSTGRES_DB", "postgres").toString
  val io = new IO()


  val database =
    Database.forURL(
      url = "jdbc:postgresql://" + connectIP + ":" + connectPort + "/" + db_name + "?serverTimezone=UTC",
      user = db_user,
      password = db_pw,
      driver = "org.postgresql.Driver")

  val gameBoardTable = new TableQuery(new GameBoardTable(_))

  override def save(gameBoard: GameBoardInterface): Unit = {
    Try {
      println("saving game in DB")
      val jsonGb = io.gameBoardToJson(gameBoard)
      val gbFromJson = (jsonGb \ "gameBoard").get.toString()
      val gb = (0, gbFromJson)
      Await.result(database.run(gameBoardTable returning gameBoardTable.map(_.id) += gb), 2.seconds)
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



