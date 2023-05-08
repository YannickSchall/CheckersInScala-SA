package restAPI

import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import controller.controllerComponent.ControllerInterface

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}

object RestUI:
  // needed to run the route
  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")

  given ActorSystem[Any] = system

  // needed for the future flatMap/onComplete in the end
  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext

  def apply(controller: ControllerInterface) =
    val routes: String =
      """
          Welcome to the View REST service! Available routes:
            GET   /ui
            GET   /undo
            GET   /redo
            POST  /ui/[param]
          """.stripMargin

    val route = concat(
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
      },
      path("ui") {
        concat(
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.gameBoard.jsonToString))
          }
        )
      },
      path("undo") {
        concat(
          get {
            UiController.undo(controller)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.gameBoard.jsonToString))
          }
        )
      },
      path("new8") {
        concat(
          get {
            UiController.new8(controller)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.gameBoard.jsonToString))
          }
        )
      },
      path("new10") {
        concat(
          get {
            UiController.new10(controller)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.gameBoard.jsonToString))
          }
        )
      },
      path("redo") {
        concat(
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.gameBoard.jsonToString))
          },
          post {
            UiController.redo(controller)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "redo success"))
          })
      },

    )
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    bindingFuture.onComplete {
      case Success(binding) => {
        val address = binding.localAddress
        println(s"View REST service online at http://localhost:${address.getPort}\nPress RETURN to stop...")
      }
      case Failure(exception) => {
        println("View REST service couldn't be started! Error: " + exception + "\n")
      }
    }



