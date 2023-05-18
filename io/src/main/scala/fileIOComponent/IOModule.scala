package fileIOComponent

import com.google.inject.AbstractModule
import fileIOComponent.IOInterface
import fileIOComponent.dbImpl.DBInterface
import fileIOComponent.dbImpl.Mongo.MongoDBCheckers
import fileIOComponent.dbImpl.Slick.SlickDBCheckers

class IOModule extends AbstractModule {
  override def configure() = {
    bind(classOf[IOInterface]).to(classOf[fileIOJsonImpl.IO])
    //bind(classOf[FileIOInterface]).to(classOf[fileIOXmlImpl.FileIO])
    //bind(classOf[DBInterface]).to(classOf[SlickDBCheckers])
    bind(classOf[DBInterface]).to(classOf[MongoDBCheckers])
  }
}
