package controller


import org.scalatest.*
import controller.controllerComponent.GameState
import controller.controllerComponent.GameState.{BLACK_TURN, BLACK_WON, WHITE_TURN, WHITE_WON}
import controller.controllerComponent.controllerBaseImpl.Controller
import controller.controllerComponent.controllerBaseImpl.FieldChanged
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.*
import utils.Mover
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.Http

import scala.swing.event.Event
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer

import scala.concurrent.Await
import scala.concurrent.duration.*
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class ControllerSpec extends AnyWordSpec {
  "A Controller" should {
    val gb = new GameBoardCreator(8).createEmptyBoard()
    val controller = new Controller(gb)
    "use undo/redo correctly" in {
      controller.gameBoard.field(0,0).isSet should be (false)
      controller.set(0,0, Piece.apply("normal", 0, 0, White))
      controller.set(4,4, Piece.apply("normal", 4, 4, White))
      controller.set(5,5, Piece.apply("normal", 5, 5, White))
      controller.set(1,1, Piece.apply("normal", 1, 1, Black))
      controller.gameBoard.field(0,0).isSet should be (true)
      controller.isSet(0,0) should be (true)
      controller.undo()
      controller.undo()
      controller.gameBoard.field(0,0).isSet should be (true)
      controller.redo()
      controller.redo()
      controller.gameBoard.field(0,0).isSet should be (true)
    }
    "have the right size" in {
      controller.gameBoardSize should be (8)
    }
    "deliver the right message about the next turn" in {
      controller.statusText should be ("It's White's turn")
      controller.setGameState(BLACK_TURN)
      controller.statusText should be ("It's Black's turn")
    }
    "move right" in {
      val gb = new GameBoardCreator(8).createEmptyBoard()
      val controller = new Controller(gb)
      controller.set(0,0, Piece.apply("normal", 0, 0, White))
      controller.set(1,1, Piece.apply("normal", 1, 1, Black))
      controller.gameBoard.field(0,0).isSet should be (true)
      controller.move("A1", "B2")
      controller.gameBoard.field(0,0).isSet should be (false)
      controller.gameBoard.field(1,1).isSet should be (true)

    }
    "be able to resize the Gameboard from 10 to 8" in {
      controller.resize(8)
      controller.gameBoardSize should be (8)
    }
    "be able to resize the Gameboard 8 to 10" in {
      controller.resize(10)
      controller.gameBoardSize should be (10)
    }
    "be unable to resize the Gameboard 10 to 12" in {
      controller.resize(12)
      controller.gameBoardSize should be (10)
    }
    "undo step" in {
      val gb = new GameBoard(8)
      val controller = new Controller(gb)
      controller.set(0,0, Piece.apply("normal", 0, 0, White))
      controller.move("A1", "C3")
    }
    "be able to move" in {
      val gb2 = new GameBoard(8)
      val controller = new Controller(gb2)
      controller.set(0,0, Piece.apply("normal", 0, 0, White))
      controller.set(4,4, Piece.apply("normal", 4, 4, White))
      controller.set(5,5, Piece.apply("normal", 5, 5, White))
      controller.set(5,5, Piece.apply("normal", 4, 3, White))
      controller.set(1,1, Piece.apply("normal", 1, 1, Black))
      controller.set(7,7, Piece.apply("normal", 7, 7, Black))
      controller.set(6,7, Piece.apply("normal", 6, 7, Black))
      controller.set(6,7, Piece.apply("normal", 6, 7, Black))
      controller.gameState should be (WHITE_TURN)
      controller.move("A1", "C3")
      controller.gameState should be (BLACK_TURN)
    }
    "be able to check if a move is possible" in {
      controller.createNewGameBoard()
      controller.set(0,0, Piece.apply("normal", 0, 0, Black))
      controller.set(4,4, Piece.apply("normal", 4, 4, Black))
      controller.set(5,5, Piece.apply("normal", 5, 5, Black))
      controller.set(1,1, Piece.apply("normal", 1, 1, White))
      controller.set(4,3, Piece.apply("normal", 4, 3, White))
      controller.set(5,3, Piece.apply("normal", 5, 3, White))

      controller.remove(2, 2)
      controller.gameState = BLACK_TURN
      val mov: Mover = controller.movePossible("A1", "C3")
      controller.move("A1", "C3")
      controller.getPiece(2,2).get.getColor should be (Black)
      mov.getBool should be (true)
      mov.getRem should be ("B2")
      mov.getQ should be (false)
      controller.movePossible("C3", "C3")
      controller.gameState should be (WHITE_TURN)
    }
    "handle a move that's not possible" in {
      controller.createNewGameBoard()
      controller.set(0, 0, Piece.apply("normal", 0, 0, White))

      val mov: Mover = controller.movePossible("B2", "C3")
      mov.getBool should be(false)
      mov.getRem should be("")
      mov.getQ should be(false)
    }
    "be able to create a GameBoard size 8" in {
      val gb = new GameBoardCreator(8).createBoard()
      controller.createGameBoard(8)
      controller.gameBoard should be (gb)
    }
    "be unable to create a new GameBoard size 10" in {
      val gb = new GameBoardCreator(12).createBoard()
      controller.createGameBoard(12)
      controller.createNewGameBoard()
      controller.gameBoard should be (gb)
    }
    "be able to do a save" in {
        val testKit: ActorTestKit = ActorTestKit()
        implicit val system: ActorSystem[Nothing] = testKit.system
        implicit val executionContext: ExecutionContextExecutor = system.executionContext

        // Create an instance of your controller implementation
        val gb = new GameBoardCreator(8).createBoard()
        val controller = new Controller(gb)
        val IOURI = controller.IOURI

        // Invoke the save() method
        controller.save()

        // Verify the HTTP request and response
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.POST,
          uri = IOURI + "/save",
          entity = gb.jsonToString
        ))

        responseFuture.onComplete {
          case Success(response) =>
            response.status.isSuccess() should be(true)
          case _ =>
        }

      }
      "be able to do a load" in {
        val testKit: ActorTestKit = ActorTestKit()
        implicit val system: ActorSystem[Nothing] = testKit.system
        implicit val executionContext: ExecutionContextExecutor = system.executionContext

        // Create an instance of your controller implementation
        val gb = new GameBoardCreator(8).createBoard()
        val controller = new Controller(gb)
        val IOURI = controller.IOURI

        // Invoke the save() method
        controller.load()

        // Verify the HTTP request and response
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.GET,
          uri = IOURI + "/load"
        ))

        responseFuture.onComplete {
          case Success(response) =>
            response.status.isSuccess() should be(true)
          case _ =>
        }

      }
      "be able to do a dbsave" in {
        val testKit: ActorTestKit = ActorTestKit()
        implicit val system: ActorSystem[Nothing] = testKit.system
        implicit val executionContext: ExecutionContextExecutor = system.executionContext

        // Create an instance of your controller implementation
        val gb = new GameBoardCreator(8).createBoard()
        val controller = new Controller(gb)
        val IOURI = controller.IOURI

        // Invoke the save() method
        controller.dbsave()

        // Verify the HTTP request and response
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.POST,
          uri = IOURI + "/dbsave",
          entity = gb.jsonToString
        ))

        responseFuture.onComplete {
          case Success(response) =>
            response.status.isSuccess() should be(true)
          case _ =>
        }

      }
      "be able to do a dbload" in {
        val testKit: ActorTestKit = ActorTestKit()
        implicit val system: ActorSystem[Nothing] = testKit.system
        implicit val executionContext: ExecutionContextExecutor = system.executionContext

        // Create an instance of your controller implementation
        val gb = new GameBoardCreator(8).createBoard()
        val controller = new Controller(gb)
        val IOURI = controller.IOURI

        // Invoke the save() method
        controller.save()

        // Verify the HTTP request and response
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.GET,
          uri = IOURI + "/dbload"
        ))

        responseFuture.onComplete {
          case Success(response) =>
            response.status.isSuccess() should be(true)
          case _ =>
        }

      }
      "dbload chat gpt" in {
        val testKit: ActorTestKit = ActorTestKit()
        implicit val system: ActorSystem[Nothing] = testKit.system
        implicit val executionContext: ExecutionContextExecutor = system.executionContext
        implicit val materializer: Materializer = Materializer.matFromSystem(system)


        // Create an instance of your controller implementation
        val gameBoard = new GameBoardCreator(8).createBoard()
        val controller = new Controller(gameBoard)
        val IOURI = controller.IOURI

        // Invoke the dbload() method
        controller.dbload()

        // Verify the HTTP request and response
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.GET,
          uri = IOURI + "/dbload"
        ))

        responseFuture.onComplete {
          case Success(response) =>
            // Verify the response
            response.status.isSuccess() should be(true)

            // Unmarshal the response entity to String
            val entityFuture: Future[String] = Unmarshal(response.entity).to[String]
            entityFuture.onComplete {
              case Success(value) =>
                // Verify unmarshalling success
                // Add more assertions if needed (e.g., content validation)
                // Use Await.result to wait for the game board conversion
                val gameBoardConversionFuture = gameBoard.jsonToGameBoard(value)
                val loadedGameBoard = Await.result(gameBoardConversionFuture, 20.seconds)

                // Verify the loaded game board
                loadedGameBoard should not be null
                // Add more assertions if needed (e.g., board size, pieces, etc.)

              case Failure(_) =>
              // Handle unmarshalling failure
            }
          case Failure(_) =>
          // Handle HTTP request failure
        }

      }

  }

}
