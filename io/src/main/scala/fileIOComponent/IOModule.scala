package fileIOComponent

import com.google.inject.AbstractModule
import fileIOComponent.IOInterface

class IOModule extends AbstractModule {
  override def configure() = {
    bind(classOf[IOInterface]).to(classOf[fileIOJsonImpl.IO])
    //bind(classOf[FileIOInterface]).to(classOf[fileIOXmlImpl.FileIO])
    //bind(classOf[DBInterface]).to(classOf[DBSlickImpl])
    //bind(classOf[DAOInterface]).to(classOf[DAOSlickGridPlayerImpl])
  }
}
