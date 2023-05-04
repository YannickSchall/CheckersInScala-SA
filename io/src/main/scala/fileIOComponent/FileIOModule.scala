package fileIOComponent

import com.google.inject.AbstractModule
import fileIOComponent.FileIOInterface

class FileIOModule extends AbstractModule {
  override def configure() = {
    bind(classOf[FileIOInterface]).to(classOf[fileIOJsonImpl.FileIO])
    //bind(classOf[FileIOInterface]).to(classOf[fileIOXmlImpl.FileIO])
  }

}
