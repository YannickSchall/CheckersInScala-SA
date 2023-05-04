package fileIOComponent
import gameboard.GameBoardInterface

trait FileIOInterface {
  def load(): String
  def save(gameAsJson: String): Unit
}
