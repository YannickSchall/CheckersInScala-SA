package fileIOComponent
import gameboard.GameBoardInterface

trait FileIOInterface {
  //def load: GameBoardInterface
  def save(gameBoard: GameBoardInterface): Unit
}
