package view


class TuiSpec extends  AnyWordSpec {
  "A tui" should {
    val controller = new Controller(new GameBoard(8))
    val tui = new Tui(controller) /*
    "create us a 8x8 gameboard when typing 'new8' " in {
      tui.tuiEntry("new 3")
      controller.gameBoard should be ("+-------+\n| O O O |\n| O O O |\n| O O O |\n+-------+")
    }
    "create us a 10x10 gameboard when typing 'new10' " in {
      tui.tuiEntry("new 10")
      controller.gameBoard should be(new GameBoard(10))
    }
    "have a size" in {
      tui.size should be (10)
    } */
  }
}