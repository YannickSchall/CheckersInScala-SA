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
import org.mongodb.scala.model.Updates.{set, setOnInsert, unset}
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.{equal, exists}
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.{Aggregates, Sorts, Updates}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import org.mongodb.scala.model.Filters
import java.io.PrintWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}

import scala.concurrent.duration.Duration.Inf
import scala.io.StdIn
import scala.util.control.Breaks.break
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Updates.*
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates
import org.mongodb.scala.MongoCollection


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
  var gameBoardCollection: MongoCollection[Document] = db.getCollection("gameBoard")

  def getNewestId(collection: MongoCollection[Document]): Int =
    val result = Await.result(collection.find(exists("_id")).sort(descending("_id")).first().head(), Inf)
    if result != null then result("_id").asInt32().getValue else 0


  override def save(gameBoard: GameBoardInterface): Unit = {
    println("Saving game in MongoDB")
    val jsonGb = io.gameBoardToJson(gameBoard)
    val gbID = getNewestId(gameBoardCollection)+1
    val gameBoardDocument: Document = Document(
      "_id" -> gbID,
      "gameBoard" -> jsonGb
    )
    Await.result(gameBoardCollection.insertOne(gameBoardDocument).asInstanceOf[SingleObservable[Unit]].head(), 5.seconds)
    println("Game saved in MongoDB")
  }

  override def load(id: Option[Int] = None): Future[Try[GameBoardInterface]] = {
    Future {
      Try {
        println("Loading game from MongoDB")

        val newID = id.getOrElse(getNewestId(gameBoardCollection))

        val gameBoardDocument = Await.result(gameBoardCollection.find(equal("_id", newID)).first().head(), 5.seconds)

        val slave = new GameBoard(8)
        val res = slave.jsonToGameBoard(gameBoardDocument("gameBoard").asString().getValue)

        println("Game loaded from MongoDB")
        res
      }
    }
  }


  override def update(id: Int, gameBoard: String): Unit = {
    Try {
      val filter = Filters.equal("_id", id)
      val update = Updates.set("gameBoard", gameBoard)
      val updateResult: Future[UpdateResult] = gameBoardCollection.updateOne(filter, update).to
      val result: UpdateResult = Await.result(updateResult, 5.seconds)
    }
  }

  override def delete(id: Int): Try[Boolean] = {
    Try {
      Await.result(gameBoardCollection.deleteOne(equal("_id", id)).asInstanceOf[SingleObservable[Unit]].head(), 5.seconds)
      true
    }
  }


}



