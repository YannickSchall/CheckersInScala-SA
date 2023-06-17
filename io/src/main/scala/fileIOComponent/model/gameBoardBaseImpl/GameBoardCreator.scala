package model.gameBoardBaseImpl

import fileIOComponent.model.GameBoardInterface
import fileIOComponent.model.gameBoardBaseImpl.{GameBoard, Piece}
import fileIOComponent.model.gameBoardBaseImpl.Color.*


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

    val blackPieceOperations: List[GameBoardInterface => GameBoardInterface] = blackPiecePositions.map {
      case (x, y) => (gb: GameBoardInterface) => gb.set(x, y, Some(Piece.apply("normal", x, y, Black)))
    }

    val operations: List[GameBoardInterface => GameBoardInterface] = noneOperations ++ whitePieceOperations ++ blackPieceOperations

    operations.foldLeft(gameBoard) { case (gb, op) => op(gb) }
  }

  def createEmptyBoard(): GameBoard = {
    val gameBoard: GameBoard = new GameBoard(size)

    val nonePositions: List[(Int, Int)] = (for {
      x <- 0 until size
      y <- 0 until size
    } yield (x, y)).toList

    val noneOperations: List[GameBoard => GameBoard] = nonePositions.map {
      case (x, y) => (gb: GameBoard) => gb.set(x, y, None)
    }

    val operations: List[GameBoard => GameBoard] = noneOperations

    operations.foldLeft(gameBoard) { case (gb, op) => op(gb) }
  }

}
