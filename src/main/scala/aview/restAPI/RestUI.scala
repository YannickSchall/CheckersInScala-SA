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


