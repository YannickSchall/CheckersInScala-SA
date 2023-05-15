package fileIOComponent.model.gameBoardBaseImpl
import fileIOComponent.model.gameBoardBaseImpl.Color.{Black, White}
import slick.ast.BaseTypedType
import scala.collection.immutable.Map
import slick.jdbc.H2Profile.MappedColumnType
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend._

enum Color(val color: String):
  case White   extends Color("white")
  case Black   extends Color("black")



final case class ColorMapping(id: Int,Color: Color)



object StreamingProvider extends Enumeration {
  type StreamingProviders = Value
  val Netflix = Value("Netflix")
  val Hulu = Value("Hulu")
}
final case class StreamingProviderMapping(
                                           id: Long,
                                           movieId: Long,
                                           streamingProvider: StreamingProvider.StreamingProviders
                                         )
