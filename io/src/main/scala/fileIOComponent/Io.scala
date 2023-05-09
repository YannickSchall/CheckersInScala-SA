package fileIOComponent

import fileIOComponent.restAPI.RestIO

import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object Io {
  @main def run = {
    var input: String = ""
    Try(RestIO) match {
      case Success(v) => println("Persistance Rest Server is running!")
      case Failure(v) => println("Persistance Server couldn't be started! " + v.getMessage + v.getCause)
    }
    while ({input = readLine(); input != "quit"}) ()
  }

}