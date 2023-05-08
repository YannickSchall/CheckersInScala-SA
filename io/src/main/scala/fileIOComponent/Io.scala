package fileIOComponent

import fileIOComponent.restAPI.RestIO

import scala.util.{Failure, Success, Try}

object Io {
  @main def run = {
    Try(RestIO) match {
      case Success(v) => println("Persistance Rest Server is running!")
      case Failure(v) => println("Persistance Server couldn't be started! " + v.getMessage + v.getCause)
    }
  }

}

