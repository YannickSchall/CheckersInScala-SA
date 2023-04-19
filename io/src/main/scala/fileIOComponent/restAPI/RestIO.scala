import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import controller.controllerComponent.Checkers

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}

object RestIO {
  val routes: String =
    """
      Welcome to the Persistence REST service! Available routes:
        GET   /fileio/load
        POST  /fileio/save
      """.stripMargin

object UiAPI:
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
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.grid.toJsonString))
    }
  },
  path("undo") {
    concat(
      get {
        UiController.undo(controller)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.grid.toJsonString))
      }
    )
  },
  path("newgame") {
    concat(
      get {
        UiController.newgame(controller)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.grid.toJsonString))
      }
    )
  },
  path("redo") {
    concat(
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.grid.toJsonString))
      },
      post {
        UiController.redo(controller)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "redo success"))
      })
  },
  path("ui" / Segment) { command => {
    UiController.drop(controller, command)
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, controller.grid.toJsonString))
  }
  }
)
val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(route)
bindingFuture.onComplete {
  case Success(binding) => {
    val address = binding.localAddress
    println(s"View REST service online at http://localhost:${address.getPort}\nPress RETURN to stop...")
  }
  case Failure(exception) => {
    println("View REST service couldn't be started! Error: " + exception + "\n")
  }
}
