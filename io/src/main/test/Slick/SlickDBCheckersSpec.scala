package Slick

import fileIOComponent.dbImpl.Slick.SlickDBCheckers
import fileIOComponent.dbImpl.Slick.Tables.GameBoardTable
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import org.mockito.Mockito.{reset, verify, when}
import org.scalactic.Prettifier.default
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, PrivateMethodTester}
import play.api.libs.json.Json
import slick.dbio.{DBIO, DBIOAction, Effect, NoStream}
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api.*
import play.api.libs.json.Json.parse
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import org.scalatest.BeforeAndAfterEach
import slick.lifted.TableQuery
import org.mockito.ArgumentMatchers.*


class SlickDBCheckersSpec extends AnyWordSpec with MockitoSugar with BeforeAndAfterEach {


  private var slickDBCheckers: SlickDBCheckers = _
  val io = new IO()


  val mockDatabase: Database = mock[Database]
  val slickDAO: SlickDBCheckers = new SlickDBCheckers {
    override val database: Database = mockDatabase
  }


  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockDatabase)
  }


  "MongoDBCheckers" should {

    "save the game board to the Slick table" in {
      //slickDBCheckers.save(gameBoard)
      val testgame = mock[GameBoardInterface]
      val _id = 1
      val gb = "a game board string"
      when(testgame.toString()).thenReturn(gb)
      when(mockDatabase.run(any)).thenReturn(_id)
      val result = slickDAO.save(testgame)
      assert(result.asInstanceOf[Try[Int]].isSuccess)
      verify(mockDatabase).run(any)



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
      slickDAO.save(gameBoard)
      val updatedGameBoard = new GameBoard(10)
      slickDAO.update(0, io.gameBoardToJson(updatedGameBoard))
      /*
      val filter = Filters.equal("_id", mongoDBCheckers.getNewestId(collection))
      val futureResult: Future[Document] = collection.find(filter).head()
      val result: Document = Await.result(futureResult, 5.seconds)
      val jsonGb = result("gameBoard").asString().getValue
      jsonGb shouldEqual mongoDBCheckers.io.gameBoardToJson(updatedGameBoard)
      */
    }

    "delete the game board from the Slick table" in {
      val gameBoard = new GameBoard(8)
      slickDBCheckers.save(gameBoard)
      val highest = 1
      val result = slickDAO.delete(1)

      /*
      val count = Await.result(collection.countDocuments().toFuture(), 5.seconds).head
      count shouldEqual highest-1
      */
    }
  }
}
