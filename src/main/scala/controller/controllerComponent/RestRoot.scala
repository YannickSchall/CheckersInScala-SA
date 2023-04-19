package controller.controllerComponent
import com.google.inject.{Guice, Injector, Inject}
import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import akka.actor.typed.ActorSystem
import scala.util.{Success,Failure}
import akka.actor.typed.scaladsl.Behaviors


object RestRoot {
    val injector: Injector = Guice.createInjector(CheckersModule())
    val controller = injector.getInstance(classOf[ControllerInterface])

    def main(args: Array[String]): Unit = {

      implicit val system = ActorSystem(Behaviors.empty, "my-system")
      implicit val executionContext = system.executionContext
      val servicePort = 8080

      val route =
        concat (
          get {
            path("load") {
              controller.load()
              complete(HttpEntity(ContentTypes.`application/json`, "loaded"))
            }
          },
          post {
            path("save") {
              controller.save()
              complete(HttpEntity(ContentTypes.`application/json`, "saved"))
            }
          }
        )

      val bound = Http().newServerAt("localhost", 8080).bind(route)

      bound.onComplete{
        case Success(binding) => {
          print(binding)
        }
        case Failure(exception) => {

        }
      }

    }

}
