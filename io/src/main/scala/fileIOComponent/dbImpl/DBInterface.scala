package fileIOComponent.dbImpl

import fileIOComponent.model.gameBoardBaseImpl.Color
import fileIOComponent.model.GameBoardInterface
trait DBInterface {

  def save(gameBoard: GameBoardInterface): Unit

  def load(id: Option[Int] = None): GameBoardInterface

  def update(id: Int, gamestate: Option[String]): Unit

  def deleteGame(id: Int): Boolean

  def load(): Unit

}
