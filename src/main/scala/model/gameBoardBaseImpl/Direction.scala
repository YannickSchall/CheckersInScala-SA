package model.gameBoardBaseImpl

enum Direction(val dir: (Int,Int)) {
  case Left extends Direction(0, -1)
  case Right extends Direction(0, 1)
  case UpLeft extends Direction(-1, -1)
  case UpRight extends Direction(-1, 1)
  case DownLeft extends Direction(1, -1)
  case DownRight extends Direction(1, 1)
}