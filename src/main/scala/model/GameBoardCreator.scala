package model

class GameBoardCreator(size:Int) {
  def createBoard(): GameBoard = {
    val gameBoard = new GameBoard(size)

    for {index <- 1 to 3} {
      for {index2 <- 0 to (size - 1)} {
        if ((index2 + index) % 2 == 0) {
          gameBoard.set(size - index, index2, Piece.apply("normal", size - index, index2, "white"))
        }
      }
    }
    for {index <- 0 to 2} {
      for {index2 <- 0 to (size - 1)} {
        if ((index2 + index) % 2 == 0) {
          gameBoard.set(index, index2, Piece.apply("normal", index, index2, "black"))
        }
      }
    }
    gameBoard
  }
}