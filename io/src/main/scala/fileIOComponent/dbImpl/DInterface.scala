package fileIOComponent.dbImpl

trait DInterface {
  def create: Unit

  def read: String

  def update(input: String): Unit

  def delete: Unit
}
