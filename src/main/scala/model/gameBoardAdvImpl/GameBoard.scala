package model.gameBoardAdvImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import model.gameBoardBaseImpl.GameBoard as BaseGameBoard

class GameBoard @Inject() (@Named("DefaultSize") size: Int) extends BaseGameBoard(size) {
}