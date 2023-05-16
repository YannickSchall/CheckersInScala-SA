package fileIOComponent

import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.GameBoard
import play.api.libs.json.Json


trait IOInterface {
  def load(): String
  def save(gameAsJson: String): Unit

  def jsonToGameBoard(source: String): GameBoard

  override def jsonToString: String
}
