package fileIOComponent.restAPI
import scala.util.{Failure, Success, Try}
import com.google.inject.AbstractModule


object Io {
  @main def run() = {
    var restPersistenceAPI = new RestIO()
    restPersistenceAPI.start()
  }


}

