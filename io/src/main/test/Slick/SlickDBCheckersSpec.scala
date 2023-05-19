package Slick
/*
import fileIOComponent.dbImpl.Mongo.MongoDBCheckers
import fileIOComponent.dbImpl.Slick.SlickDBCheckers
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observable, ObservableFuture}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.gridfs.ObservableFuture
import org.mongodb.scala.model.Aggregates.*
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.*
import org.mongodb.scala.model.Updates.*
import org.scalactic.Prettifier.default
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, PrivateMethodTester}
import slick.dbio.{DBIO, DBIOAction, Effect, NoStream}
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class MongoDBCheckersSpec extends AnyWordSpec with Matchers with BeforeAndAfter {

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

  private var slickDBCheckers: SlickDBCheckers = _
  val io = new IO()


  before {
    slickDBCheckers = new SlickDBCheckers()
    val gameBoardTable = new TableQuery(new GameBoardTable(_))
    val setup: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(
      gameBoardTable.schema.createIfNotExists
    )
  }

  after {
  }

  "MongoDBCheckers" should {

    "save the game board to the Slick table" in {
      val gameBoard = new GameBoard(8)
      slickDBCheckers.save(gameBoard)

      val result = Await.result(collection.find().first().head(), 5.seconds)
      result("gameBoard").asString().getValue shouldEqual io.gameBoardToJson(gameBoard)
    }

    "load the game board from the Slick table" in {
      val gameBoard = new GameBoardCreator(8).createBoard()
      slickDBCheckers.save(gameBoard)
      val loadedGameBoard = slickDBCheckers.load()
      loadedGameBoard.isSuccess shouldEqual true
      loadedGameBoard.get.toJson shouldEqual gameBoard.toJson
    }

    "update the game board in the Slick table" in {
      val gameBoard = new GameBoard(8)
      slickDBCheckers.save(gameBoard)
      val updatedGameBoard = new GameBoard(10)
      slickDBCheckers.update(0, io.gameBoardToJson(updatedGameBoard))

      val filter = Filters.equal("_id", mongoDBCheckers.getNewestId(collection))
      val futureResult: Future[Document] = collection.find(filter).head()
      val result: Document = Await.result(futureResult, 5.seconds)
      val jsonGb = result("gameBoard").asString().getValue
      jsonGb shouldEqual mongoDBCheckers.io.gameBoardToJson(updatedGameBoard)
    }

    "delete the game board from the Slick table" in {
      val gameBoard = new GameBoard(8)
      slickDBCheckers.save(gameBoard)
      val highest = 1
      val result = slickDBCheckers.delete(1)
      result.isSuccess shouldEqual true
      result.get shouldEqual true

      val count = Await.result(collection.countDocuments().toFuture(), 5.seconds).head
      count shouldEqual highest-1
    }
  }
}
*/