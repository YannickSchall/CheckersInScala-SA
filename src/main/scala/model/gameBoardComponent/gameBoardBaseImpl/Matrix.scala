package model.gameBoardComponent.gameBoardBaseImpl

import scala.annotation.targetName

case class Matrix[Field] (lines: Vector[Vector[Field]]) {
  def this(size: Int, filling:Field) = this(Vector.tabulate(size, size) { (row,col) => filling })
  val size: Int = lines.size
  @targetName("copyMatrix")
  def copy(lines: Vector[Vector[Field]]) = new Matrix[Field](lines: Vector[Vector[Field]])
  def field(row: Int, col: Int): Field = lines(row)(col)
  def replaceField(row: Int, col: Int, field: Field): Matrix[Field] = {
    copy(lines.updated(row, lines(row).updated(col, field)))
  }
}
