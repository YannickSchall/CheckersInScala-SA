package fileIOComponent.restAPI
import scala.util.{Failure, Success, Try}

class Io {
  var input: String = ""
  Try(RestIO) match
    case Success(v) => println("Persistance Rest Server is running!")
    case Failure(v) => println("Persistance Server couldn't be started! " + v.getMessage + v.getCause)

}
