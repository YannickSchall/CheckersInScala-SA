package model.gameBoardComponent.gameBoardBaseImpl

import model.gameBoardComponent.GameBoardInterface
import model.gameBoardComponent.gameBoardBaseImpl.Color.*


class GameBoardCreator(size: Int) {
  def createBoard(): GameBoardInterface = {
    val gameBoard: GameBoardInterface = new GameBoard(size)

    val nonePositions: List[(Int, Int)] = (for {
      x <- 0 until size
      y <- 0 until size
    } yield (x, y)).toList

    val whitePiecePositions: List[(Int, Int)] = (for {
      x <- size - 3 until size
      y <- 0 until size
      if (y + x) % 2 == 0
    } yield (x, y)).toList

    val blackPiecePositions: List[(Int, Int)] = (for {
      x <- 0 until 3
      y <- 0 until size
      if (y + x) % 2 == 0
    } yield (x, y)).toList

    val noneOperations: List[GameBoardInterface => GameBoardInterface] = nonePositions.map {
      case (x, y) => (gb: GameBoardInterface) => gb.set(x, y, None)
    }

    val whitePieceOperations: List[GameBoardInterface => GameBoardInterface] = whitePiecePositions.map {
      case (x, y) => (gb: GameBoardInterface) => gb.set(x, y, Some(Piece.apply("normal", x, y, White)))
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