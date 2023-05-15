package fileIOComponent.dbImpl.Slick.Tables
import slick.jdbc.MySQLProfile.api.*
import fileIOComponent.model.gameBoardBaseImpl.Piece

class GameBoardTable(tag: Tag) extends Table[(Int, String)](tag, "GAMEBOARD"){
    def id = column[Int] ("ID", O.PrimaryKey, O.AutoInc)

    def gamestate = column[String]
    
    def * = (id, gamestate)
}
  // Grid(Cells(Object(row, col, player) - player . settings . grid
  // Gameboard(Gamestate(Field(Piece(row, col, state Color))

  // laden , speicher, redo, undo, delete, create, ->
  // TAB0: id, size, FeldNmr
  // TAB1: id, gamestate (w/b) , PieceID( pcolor, prow, pstate),
  // TAB2: id, pcolor, prow, pstate






// GAME TABLE
// ROW    COL   PIECE_ID
// ROW    COL   NONE
// ROW    COL   PIECE_ID

// delete -> setAll Piece id to none
// create -> delete + steinesetzten
//

// GAME TABLE (REAL)
// 0    0   PIECE_ID
// 0    1   NONE
// 0    2   PIECE_ID
// 1    0   PIECE_ID
// 1    1   NONE

// PIECE TABLE
// PIECE_ID   PCOLOR  PSTATE
// PIECE_ID   PCOLOR  PSTATE
// PIECE_ID   PCOLOR  PSTATE


// PIECE TABLE (REAL)
// 1   white  normal
// 2   black  normal
// 3   black  queen