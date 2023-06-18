# CheckersInScala

[![Coverage Status](https://coveralls.io/repos/github/simonwinter-git/CheckersInScala/badge.svg?branch=master)](https://coveralls.io/github/simonwinter-git/CheckersInScala?branch=master)

## Set Up

### Docker Compose Instruction:
Please make sure to follow all steps closely, to get the game running on your system:

**Windows:**
1. Install X Launch https://sourceforge.net/projects/xming/
   start > multiple windows > start no client > tick **Disable Access control** > finish

2. Open your cmd > type ipconfig > look for your IPv4/6 > modify in CheckersInScala-SA/Dockerfile ENV DISPLAY=X.X.X.X:0.0

3. Run docker compose script


**MacOS:**

1. Download XQuartz https://www.xquartz.org/

2. Open terminal > enter xhost+

3. Open your cmd > type ipconfig > look for your IPv4/6 > modify in CheckersInScala-SA/Dockerfile ENV DISPLAY=X.X.X.X:0.0


## Game Instructions
WELCOME TO CHECKERS

How to win:
- The game ends if one player has less than 2 pieces or is unable to move with any of their pieces

Preparation:
- You are playing on a checkered board with the classic size 8 x 8 or the more common size 10 x 10
- You can choose between white/black or red/black coloring
- Only the non-black fields are used
- Every player gets pieces on the first 3 rows on every other field

<img src="/src/main/Resources/Bild1.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild2.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild3.png?raw=true" width = "275" height = "275">
<br />

The moves:
- A normal piece can only move diagonally forward
- If there is an opponent's piece on the field right next to yours (diagonally) and the field behind it is empty, you can capture the opponent's piece by jumping over and removing it from the gameboard
- If you want to move a piece, but the piece is able to capture an opponent's piece, you have to capture it instead:

<img src="/src/main/Resources/Bild4.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild5.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild6.png?raw=true" width = "275" height = "275">
<br />

- If there is an opportunity to capture more pieces with the same one you initially used, you have to capture those as well:

<img src="/src/main/Resources/Bild7.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild8.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild9.png?raw=true" width = "275" height = "275">
<br />

- Once a piece reaches the other end of the board, it turns into a queen. However, you can only move with it when it's your turn again
- The queen can move into every diagonal direction and travel as many fields as you want, as long as the fields are empty
- If there is an opponent's piece in the queen's way, the queen can capture it over the whole distance of its move, assuming the piece behind it is empty:

<img src="/src/main/Resources/Bild11.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild10-2.png?raw=true" width = "275" height = "275"> <img src="/src/main/Resources/Bild12.png?raw=true" width = "275" height = "275">
<br />

CONTROLS:
-

There are 2 ways to make your moves:
- Using your mouse to move the pieces:
  - Click on the piece you want to move and its field is going to be highlighted
  - Now click on the field you want to move your piece. If the move is illegal, the terminal will tell you!

- Typing your moves into the terminal:
  - Inputs are case-insensitive, so every combination of capitalization will be registered the same way, e.g.: mOVe b8 C7 == move b8 c7
  - If you need help, simply typing 'help' is your way to go
  - 'new 8' or 'new 10' creates a new game board with the given size
  - 'move XX YY' moves a piece from a field to a new field: Example: move B8 C7
  - If you are not sure about a move, use 'try XX YY' to check if a move is legal
  - There are a few more other commands you can find out with 'help'

Have fun
