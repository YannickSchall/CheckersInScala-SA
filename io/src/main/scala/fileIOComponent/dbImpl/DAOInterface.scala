package fileIOComponent.dbImpl

trait DAOInterface {
  def create: Unit

  def read: String

  def update(input: String): Unit

  def delete: Unit



}
