package fileIOComponent.dbImpl.Slick.Tables
import slick.jdbc.MySQLProfile.api.*
import fileIOComponent.model.gameBoardBaseImpl.Piece

class GameBoardTable(tag: Tag) extends Table[(Int, String)](tag, "GAMEBOARD"){
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def gamestate = column[String]("GAMESTATE")
    
    override def * = (id, gamestate)
}
