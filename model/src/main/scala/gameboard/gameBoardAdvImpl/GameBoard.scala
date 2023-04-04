package gameboard.gameBoardAdvImpl
import com.google.inject.Inject
import com.google.inject.name.Named
import gameboard.gameBoardBaseImpl.{GameBoard => BaseGameBoard}

class GameBoard @Inject() (@Named("DefaultSize") size: Int) extends BaseGameBoard(size) {
}