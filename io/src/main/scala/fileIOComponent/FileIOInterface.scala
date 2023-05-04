package fileIOComponent

trait FileIOInterface {
  def load(): String
  def save(gameAsJson: String): Unit
}
