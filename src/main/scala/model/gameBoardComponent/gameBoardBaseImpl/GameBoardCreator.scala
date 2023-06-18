package model.gameBoardComponent.gameBoardBaseImpl

import model.gameBoardComponent.GameBoardInterface
import model.gameBoardComponent.gameBoardBaseImpl.Color.*

class GameBoardCreator(size:Int) {
  def createBoard(): GameBoardInterface = {
    var gameBoard:GameBoardInterface = new GameBoard(size)

    for {index <- 0 until size} {
      for {index2 <- 0 until size} {
        gameBoard = gameBoard.set(index, index2, None)
      }
    }

    for {index <- 1 to 3} {
      for {index2 <- 0 until size} {
        if ((index2 + index) % 2 == 0) {
          gameBoard = gameBoard.set(size - index, index2, Some(Piece.apply("normal", size - index, index2, White)))
        }
      }
    }
    for {index <- 0 to 2} {
      for {index2 <- 0 until size} {
        if ((index2 + index) % 2 == 0) {
          gameBoard = gameBoard.set(index, index2, Some(Piece.apply("normal", index, index2, Black)))
        }
      }
    }
    gameBoard
  }
}