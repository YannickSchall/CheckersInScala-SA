package fileIOComponent
import gameboard.GameBoardInterface

trait FileIOInterface {
  def load(gameBoard: GameBoardInterface): GameBoardInterface
  def save(gameBoard: GameBoardInterface): Unit
}
