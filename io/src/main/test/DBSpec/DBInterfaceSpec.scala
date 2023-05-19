package DBSpec

import org.scalatest.*
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.model.{GameBoardInterface, gameBoardBaseImpl}
import org.scalatest.matchers.should.Matchers
import fileIOComponent.model.gameBoardBaseImpl.{GameBoard, GameBoardCreator}
import org.scalatest.matchers.should.Matchers.{a, should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import fileIOComponent.dbImpl.Mongo.MongoDBCheckers
import fileIOComponent.fileIOJsonImpl.IO

import util.{Failure, Success, Try}


class DBInterfaceSpec extends AnyWordSpec with Matchers {

  val dbInterface: DBInterface = new MongoDBCheckers()

  "DBInterface" should {

    "save and load a game board successfully" in {
      val gameBoard = new GameBoardCreator(8).createBoard()
      val id = 1
      val io = new IO()

      // Save the game board
      dbInterface.save(gameBoard)

      // Load the game board
      val loadResult = dbInterface.load(Some(id))

      // Assert the result
      loadResult shouldBe a[Success[_]]
      io.gameBoardToJson(loadResult.get) shouldBe io.gameBoardToJson(gameBoard)
    }

    "return a Failure when loading a non-existent game board" in {
      val nonExistentId = 999

      // Load the non-existent game board
      val loadResult = dbInterface.load(Some(nonExistentId))

      // Assert the result
      loadResult shouldBe a[Failure[_]]
    }

    "update a game board successfully" in {
      val id = 1
      val updatedGameState = "Updated game state"

      // Update the game board
      dbInterface.update(id, updatedGameState)

      // Load the updated game board
      val loadResult = dbInterface.load(Some(id))

      // Assert the result
      loadResult shouldBe a[Success[_]]
      //loadResult.get.getGameState shouldBe updatedGameState
    }

    "delete a game board successfully" in {
      val id = 1

      // Delete the game board
      val deleteResult = dbInterface.delete(id)

      // Assert the result
      deleteResult shouldBe a[Success[_]]
      deleteResult.get shouldBe true

      // Load the deleted game board
      val loadResult = dbInterface.load(Some(id))

      // Assert the result
      loadResult shouldBe a[Failure[_]]
    }

  }

}