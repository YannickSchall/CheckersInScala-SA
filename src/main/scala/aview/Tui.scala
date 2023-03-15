package aview
import java.awt.GraphicsEnvironment

import controller.controllerComponent._
import controller.controllerComponent.controllerBaseImpl.PrintTui
import model.gameBoardComponent.gameBoardBaseImpl.Piece

import scala.swing.Reactor
import scala.util.{Failure, Success, Try}

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)

  def size = controller.gameBoardSize

  def tuiEntry(inputSens: String): Unit = {
    def input = inputSens.toUpperCase
    def args:Array[String] = input.split(" ")
    args(0) match {
      case "NEW" =>
        Try {args(1).toInt} match {
          case Failure(e) => println("Error: Non integer " + e.getMessage.charAt(0).toLower + e.getMessage.tail + "\nTry 8 or 10\n")
          case Success(e) => if (Integer.parseInt(args(1)) == 8 || Integer.parseInt(args(1)) == 10) controller.createGameBoard(args(1).toInt) else print("Try 8 or 10\n")
        }
      case "SAVE" => controller.save; print("Progress saved\n")
      case "LOAD" => controller.load; print("Progress loaded\n")
      case "REMOVE" => controller.remove(Integer.parseInt(args(1).tail)-1, args(1).charAt(0).toInt - 65)
      case "UNDO" => controller.undo
      case "REDO" => controller.redo
      case "FONTS" => val fonts: Array[String] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); for (i <- fonts) {print(i + "\n") }
      case "QUIT" => System.exit(0)
      case "MOVE" => if (controller.movePossible(args(1), args(2)).getBool) if (controller.movePossible(args(1), args(2)).getBool) {
        val row = Integer.parseInt(args(2).tail)-1
        val col = args(2).charAt(0).toInt - 65
        var rem = false
        var which = ""
        if (!controller.movePossible(args(1), args(2)).getRem.isEmpty && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(args(1).tail) - 1, args(1).charAt(0).toInt - 65).get.getColor.charAt(0).toString) rem = true; which = controller.movePossible(args(1), args(2)).getRem
        if (controller.movePossible(args(1), args(2)).getQ && controller.gameState.toString.charAt(0).toString.toLowerCase == controller.getPiece(Integer.parseInt(args(1).tail) - 1, args(0).charAt(1).toInt - 65).get.getColor.charAt(0).toString) {
          print(controller.getPiece(Integer.parseInt(args(1).tail) - 1, args(1).charAt(0).toInt - 65).get.getColor.charAt(0).toString)
          controller.move(args(1), args(2))
          controller.set(row, col, Piece("queen", row, col, controller.getPiece(row, col).get.getColor))
          if (rem) controller.remove(Integer.parseInt(which.tail)-1, which.charAt(0).toInt - 65)
        } else controller.move(args(1), args(2))
      } else print("Move not possible\n")

      case "HELP" =>
        print("\nThis is the help menu with information about possible TUI inputs.\nThe inputs are case-insensitive, so every combination of capitalization will be registered the same way.\n")
        print("\n\u001B[32mnew\u001B[0m: Creates a new Gameboard, filled with Pieces. \u001B[35mUsage\u001B[0m: \"\u001B[33mnew X\u001B[0m\", where X = either 8 or 10.\n")
        print("\u001B[32msave\u001B[0m: Saves the current state of the game in a .json file. It can be loaded at a later point using \"\u001B[32mload\u001B[0m\".\n")
        print("\u001B[32mload\u001B[0m: Loads a state of the game that was saved beforehand using \"\u001B[32msave\u001B[0m\".\n")
        print("\u001B[32mtry\u001B[0m: Returns a Boolean value of whether a certain move is possible or not. \u001B[35mUsage\u001B[0m: \"\u001B[33mtry XX YY\u001B[0m\", where XX and YY = field names, such as \"A1\" and \"B2\".\n")
        print("\u001B[32mmove\u001B[0m: Moves a piece from one field to another, capturing opponent's pieces in the process. \u001B[35mUsage\u001B[0m: \"\u001B[33mmove XX YY\u001B[0m\", where XX and YY = field names, such as \"A1\" and \"B2\".\n")
        print("\u001B[32mremove\u001B[0m: Removes a piece from a specified field. \u001B[35mUsage\u001B[0m: \"\u001B[33mremove XX\u001B[0m\", where XX = a field name, such as \"A1\".\n")
        print("\u001B[32mundo\u001B[0m: The last move will be undone. This works any amount of times until you reach the Game's initial state or the last \u001B[32mload\u001B[0m.\n")
        print("\u001B[32mredo\u001B[0m: The last undone move will be redone. This works any amount of times until you reach the Game's latest state again.\n")
        print("\u001B[32mgetColor\u001B[0m: Returns the color of a piece at a specified location; mainly used for debugging purposes. \u001B[35mUsage\u001B[0m: \"\u001B[33mgetColor XX\u001B[0m\", where XX = a field name, such as \"A1\".\n")
        print("\u001B[32mhelp\u001B[0m: At this point you probably already know about it, but it prints information about possible TUI inputs.\n\n")

      case "TRY" => print(controller.movePossible(args(1), args(2)).toString + "\n")
      case "GETCOLOR" => print(controller.getPiece(args(1).charAt(1).toInt - 49, args(1).charAt(0).toInt - 65).get.getColor + "\n")
      case _ => print("Try something else, for possible inputs, type \"help\"\n")
    }
  }

  reactions += {
    case event: PrintTui => printTui
  }

  def printTui: Unit = {
    println(controller.gameBoardToString)
    println(GameState.message(controller.gameState))
  }
}