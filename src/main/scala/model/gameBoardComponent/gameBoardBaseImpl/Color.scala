package model.gameBoardComponent.gameBoardBaseImpl

enum Color(val color: Boolean):
  case White   extends Color(true)
  case Black   extends Color(false)

