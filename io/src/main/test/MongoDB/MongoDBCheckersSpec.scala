package MongoDB
import org.mongodb.scala.Document
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.Observable
import org.mongodb.scala.model.Filters.{equal, exists, *}
import org.mongodb.scala.model.Sorts.*
import org.mongodb.scala.model.Updates.*
import org.mongodb.scala.model.Aggregates.*

import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import fileIOComponent.dbImpl.Mongo.MongoDBCheckers
import fileIOComponent.model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import fileIOComponent.fileIOJsonImpl.IO
import org.scalatest.{BeforeAndAfter, PrivateMethodTester}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalactic.Prettifier.default
import org.mongodb.scala.ObservableFuture
import org.mongodb.scala.gridfs.ObservableFuture
import org.mongodb.scala.model.Filters
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.mongodb.scala.Document
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.Observable

class MongoDBCheckersSpec extends AnyWordSpec with Matchers with BeforeAndAfter {

  val databaseUser: String = sys.env.getOrElse("MONGO_INITDB_ROOT_USERNAME", "root")
  val databasePassword: String = sys.env.getOrElse("MONGO_INITDB_ROOT_PASSWORD", "mongo")
  val databaseHost: String = sys.env.getOrElse("MONGO_INITDB_HOST", "localhost")
  val databasePort: String = sys.env.getOrElse("MONGO_INITDB_PORT", "27017")
  val databaseUrl = s"mongodb://$databaseUser:$databasePassword@$databaseHost:$databasePort/?authSource=admin"


  private val testDatabase: String = "test_checkers"
  private val testCollection: String = "test_gameBoard"

  private var client: MongoClient = _
  private var db: MongoDatabase = _
  private var collection: MongoCollection[Document] = _

  private var mongoDBCheckers: MongoDBCheckers = _
  val io = new IO()

  before {
    client = MongoClient(databaseUrl)
    db = client.getDatabase(testDatabase)
    collection = db.getCollection(testCollection)
    mongoDBCheckers = new MongoDBCheckers()
    mongoDBCheckers.gameBoardCollection = collection
  }

  after {
    collection.drop()
    client.close()
  }

  "MongoDBCheckers" should {

    "save the game board to the MongoDB collection" in {
      val gameBoard = new GameBoard(8)
      mongoDBCheckers.save(gameBoard)
      val result = Await.result(collection.find().first().head(), 5.seconds)
      result("gameBoard").asString().getValue shouldEqual io.gameBoardToJson(gameBoard)
    }

    "load the game board from the MongoDB collection" in {
      val gameBoard = new GameBoardCreator(8).createBoard()
      mongoDBCheckers.save(gameBoard)
      val loadedGameBoard = mongoDBCheckers.load()
      loadedGameBoard.isSuccess shouldEqual true
      loadedGameBoard.get.toJson shouldEqual gameBoard.toJson
    }

    "update the game board in the MongoDB collection" in {
      val gameBoard = new GameBoard(8)
      mongoDBCheckers.save(gameBoard)
      val updatedGameBoard = new GameBoard(10)
      mongoDBCheckers.update(mongoDBCheckers.getNewestId(collection), io.gameBoardToJson(updatedGameBoard))
      val filter = Filters.equal("_id", mongoDBCheckers.getNewestId(collection))
      val futureResult: Future[Document] = collection.find(filter).head()
      val result: Document = Await.result(futureResult, 5.seconds)
      val jsonGb = result("gameBoard").asString().getValue
      jsonGb shouldEqual mongoDBCheckers.io.gameBoardToJson(updatedGameBoard)
    }

    "delete the game board from the MongoDB collection" in {
      val gameBoard = new GameBoard(8)
      mongoDBCheckers.save(gameBoard)
      val highest = mongoDBCheckers.getNewestId(collection)
      val result = mongoDBCheckers.delete(1)
      result.isSuccess shouldEqual true
      result.get shouldEqual true

      val count = Await.result(collection.countDocuments().toFuture(), 5.seconds).head
      count shouldEqual highest-1
    }
  }
}