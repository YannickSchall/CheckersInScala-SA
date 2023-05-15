package fileIOComponent.restAPI
import com.google.inject.{Guice, Inject, Injector}
import fileIOComponent.dbImpl.{DBInterface, DAOInterface}
import fileIOComponent.{IOInterface, IOModule}

import java.io.*
import play.api.libs.json.{JsValue, Json}

import scala.io.Source


object IOController {

  val injector: Injector = Guice.createInjector(IOModule())
  val fileIO = injector.getInstance(classOf[IOInterface])
  val database = injector.getInstance(classOf[DBInterface])
  val databaseDAO = injector.getInstance(classOf[DAOInterface])

  def load(): String = {
    fileIO.load()
  }

   def save(gameAsJson: String) = {
     fileIO.save(gameAsJson)
   }

}
