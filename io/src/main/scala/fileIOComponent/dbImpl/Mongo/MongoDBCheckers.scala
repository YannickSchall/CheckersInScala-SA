package fileIOComponent.dbImpl.Mongo

import com.google.inject.Inject
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.{Color, GameBoard, Piece}
import play.api.libs.json.JsPath.\
import play.api.libs.json.JsValue
import play.api.libs.json.Json.parse
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.{equal, exists}
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.{Aggregates, Sorts}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import java.io.PrintWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration.Inf
import scala.io.StdIn
import scala.util.control.Breaks.break
import scala.util.{Failure, Success, Try}


class MongoDBCheckers @Inject() extends DBInterface {
  val databaseDB: String = sys.env.getOrElse("MONGO_INITDB_DATABASE", "checkers")
  val databaseUser: String = sys.env.getOrElse("MONGO_INITDB_ROOT_USERNAME", "root")
  val databasePassword: String = sys.env.getOrElse("MONGO_INITDB_ROOT_PASSWORD", "mongo")
  val databaseHost: String = sys.env.getOrElse("MONGO_INITDB_HOST", "localhost")
  val databasePort: String = sys.env.getOrElse("MONGO_INITDB_PORT", "27017")
  val databaseUrl = s"mongodb://$databaseUser:$databasePassword@$databaseHost:$databasePort/?authSource=admin"
  println(databaseUrl)
  val io = new IO()
  val client: MongoClient = MongoClient(databaseUrl)
  val db: MongoDatabase = client.getDatabase("Checkers")
  val gameBoardCollection: MongoCollection[Document] = db.getCollection("gameBoard")

  private def getNewestId(collection: MongoCollection[Document]): Int =
    println("start getting id")
    val result = Await.result(collection.find(exists("_id")).sort(descending("_id")).first().head(), Inf)
    println("done getting id")
    println(": "+result("_id").asInt32().getValue)
    if result.contains("_id") then result("_id").asInt32().getValue else 0


    //val result = Await.result(, Inf)

    //result.flatMap(_.get("_id").map(_.asInt32().getValue.toHexString)).getOrElse("0").toInt

  override def save(gameBoard: GameBoardInterface): Unit = {
    println("Saving game in MongoDB")
    val jsonGb = io.gameBoardToJson(gameBoard)
    println("1")
    val gbID = getNewestId(gameBoardCollection)+1
    println("2")
    val gameBoardDocument: Document = Document(
      "_id" -> gbID,
      "gameBoard" -> jsonGb
    )
    println("3")
    Await.result(gameBoardCollection.insertOne(gameBoardDocument).asInstanceOf[SingleObservable[Unit]].head(), 5.seconds)
    println("Game saved in MongoDB")
  }

  override def load(id: Option[Int] = None): Try[GameBoardInterface] = {
    Try {
      println("Loading game from MongoDB")

      val newID = id match {
        case Some(id) => id
        case None => getNewestId(gameBoardCollection)
      }
      val gameBoardDocument = Await.result(gameBoardCollection.find(equal("_id", newID)).first().head(), 5.seconds)

      val slave = new GameBoard(8)
      val res = try {
        slave.jsonToGameBoard(gameBoardDocument("gameBoard").asString().getValue)
      } catch {
        case e: Exception =>
          e.printStackTrace()
          throw e
      }
      println("Game loaded from MongoDB")
      res
    }
  }

  override def update(id: Int, gameBoard: String): Unit = {
    Try {
      gameBoardCollection.updateOne(equal("_id","gameBoardDocument"), set("_id", gameBoard))
      //Await.result(gameBoardCollection.find(equal("_id", gameBoard)).first().head(), 5.seconds)
    }
  }

  override def delete(id: Int): Try[Boolean] = {
    Try {
      Await.result(gameBoardCollection.deleteOne(equal("_id", id)).asInstanceOf[SingleObservable[Unit]].head(), 5.seconds)
      true
    }
  }

}



