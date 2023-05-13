package fileIOComponent.restAPI
import com.google.inject.{Guice, Inject, Injector}
import fileIOComponent.dbImpl.{DBInterface, DInterface}
import fileIOComponent.{FileIOInterface, FileIOModule}

import java.io.*
import play.api.libs.json.{JsValue, Json}

import scala.io.Source


object FileIOController {

  val injector: Injector = Guice.createInjector(FileIOModule())
  val fileIO = injector.getInstance(classOf[FileIOInterface])
  val database = injector.getInstance(classOf[DBInterface])
  val databaseDAO = injector.getInstance(classOf[DInterface])

  def load(): String = {
    fileIO.load()
  }

   def save(gameAsJson: String) = {
     fileIO.save(gameAsJson)
   }

}
