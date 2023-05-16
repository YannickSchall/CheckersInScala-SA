package fileIOComponent.restAPI

import akka.http.scaladsl.server.Directives.{complete, concat, get, path}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import fileIOComponent.restAPI.IOController
import fileIOComponent.dbImpl.Slick.SlickDBCheckers

import scala.util.{Failure, Success}
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File

import scala.concurrent.{ExecutionContextExecutor, Future}
import com.google.inject.AbstractModule
import fileIOComponent.fileIOJsonImpl.IO
import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.GameBoard

import scala.io.StdIn


object RestIO {
  val routes: String =
    """
      Welcome to the Persistence REST service! Available routes:
        GET   /fileio/load
        POST  /fileio/save
      """.stripMargin


  val fileIO = new IO
  val slick = new SlickDBCheckers

  // needed to run the route
  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")

  given ActorSystem[Any] = system

  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext

  val connectIP = sys.env.getOrElse("FILEIO_SERVICE_HOST", "localhost").toString
  val connectPort = sys.env.getOrElse("FILEIO_SERVICE_PORT", 8081).toString.toInt


  val route = concat(
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
    },
    path("io" / "load") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, IOController.load()))
      }
    },
    path("io" / "dbload") {
      get {
        parameter("id".?) { (id) =>
          val id_updated =
            id match {
              case Some(id) => Some(id.toInt)
              case None => None
            }
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, fileIO.gameBoardToJson(
            slick.load(id_updated).getOrElse(new GameBoard(1))
              )
            )
          )
        }
      }
    },
    path("io" / "save") {
      concat(
        post {
          entity(as[String]) { game =>
            IOController.save(game)
            complete("game saved")
          }
        }
      )
    },
    path ("io" / "dbsave") {
    concat(
      post {
        entity(as[String]) { gameString =>
          slick.save(fileIO.jsonToGameBoard(gameString))
          complete("game saved")
        }
      }
    )
    },
    path("io" / "dbdelete") {
        put {
          parameter("id") {
            (id) =>
              complete {
                val result = slick.deleteGame(id = id.toInt).getOrElse("ERROR")
                Future.successful(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"deleted game in game database" +
                  s" $result"))
              }
          }
        }
    },
    path("io" / "dbupdate") {
      put {
        parameter("id", "gamestate".?) {
          (id, gamestate) =>
            complete {
              val updatetdGb =
                gamestate match {
                  case Some(gamestate) => Some(gamestate.toString)
                  case None => None
                }
              val result = slick.update(id = id.toInt, gamestate=gamestate.toString)
              Future.successful(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"updated gameboard database" +
                s" $result"))
            }
        }
      }
    }
  )


    val bindingFuture = Http().newServerAt(connectIP, connectPort).bind(route)

    bindingFuture.onComplete {
      case Success(binding) => {
        val address = binding.localAddress
        println(s"IO REST service online at http://$connectIP:$connectPort\nPress RETURN to stop...")
        StdIn.readLine() // let it run until user presses return
        bindingFuture
          .flatMap(_.unbind()) // trigger unbinding from the port
          .onComplete(_ => system.terminate()) // and shutdown when done
      }
      case Failure(exception) => {
        println("IO REST service couldn't be started! Error: " + exception + "\n")
      }
        
  }
}