package fileIOComponent.dbImpl

import fileIOComponent.model.gameBoardBaseImpl.Color
import fileIOComponent.model.GameBoardInterface
import scala.util.{Failure, Success, Try}

trait DBInterface {

  def save(gameBoard: GameBoardInterface): Unit

  def load(id: Option[Int] = None): Try[GameBoardInterface]

  def update(id: Int, gamestate: String): Unit

  def deleteGame(id: Int): Try[Boolean]

}
