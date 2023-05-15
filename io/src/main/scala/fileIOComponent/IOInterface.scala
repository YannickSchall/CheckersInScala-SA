package fileIOComponent

trait IOInterface {
  def load(): String
  def save(gameAsJson: String): Unit
}
