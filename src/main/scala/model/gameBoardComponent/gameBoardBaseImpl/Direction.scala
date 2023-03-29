package model.gameBoardComponent.gameBoardBaseImpl

enum Direction(val dir: String) {
  case Left extends Direction("left")
  case Right extends Direction("right")
  case UpLeft extends Direction("up_left")
  case UpRight extends Direction("up_right")
  case DownLeft extends Direction("down_left")
  case DownRight extends Direction("down_right")
}