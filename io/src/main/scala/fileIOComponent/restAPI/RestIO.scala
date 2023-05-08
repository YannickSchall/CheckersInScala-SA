package fileIOComponent.restAPI
import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import fileIOComponent.restAPI.FileIOController
import scala.util.{Failure, Success}
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File
import scala.concurrent.{ExecutionContextExecutor, Future}
import com.google.inject.AbstractModule


class RestIO {
  val routes: String =
    """
      Welcome to the Persistence REST service! Available routes:
        GET   /fileio/load
        POST  /fileio/save
      """.stripMargin

  // needed to run the route
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext


  val route = concat(
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
    },
    path("fileio" / "load") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, FileIOController.load()))
      }
    },
    path("fileio" / "save") {
      concat(
        post {
          entity(as[String]) { game =>
            FileIOController.save(game)
            complete("game saved")
          }
        }
      )
    }
  )

  def start(): Unit = {
    val bindingFuture = Http().newServerAt("0.0.0.0", 8081).bind(route)

    bindingFuture.onComplete {
      case Success(binding) => {
        val address = binding.localAddress
        println(s"File IO REST service online at http://localhost:${address.getPort}\nPress RETURN to stop...")
      }
      case Failure(exception) => {
        println("File IO REST service couldn't be started! Error: " + exception + "\n")
      }

  }


  }
}