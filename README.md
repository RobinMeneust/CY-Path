# CY-Path

The Quoridor game in java

## Description

CY-Path is a student game project developed in Java. It uses JavaFX for the graphics. 
Quoridor can be played by either 2 or 4 players.

## Authors

* BARRE Romain
* ETRILLARD Yann
* GARCIA-MEGEVAND Thibault
* KUSMIDER David
* MENEUST Robin

## Important

Please note that CYPath.jar needs to be with the lib directory in target whereas CYPathStandalone.jar doesn't

## Installation

- Java must be installed (was tested on Java 19.0.2)
- JavaFX must be installed (JDK version) (was tested on 17.0.7)
- Maven is required (was tested on 3.9.2)

Change `19` to your JDK version in pom.xml at the line :
`<java.version>19</java.version>`

Change `17.0.7` to your JavaFX version in pom.xml at the line :
`<javafx.version>17.0.7</javafx.version>`


## Compile and generate jar executable

`mvn clean prepare-package jar:jar package` to create the executables .jar (CYPathStandalone has the .java of JavaFX classes inside it, but we still haven't checked if it's really standalone)

or just `mvn clean prepare-package jar:jar` to create the not standalone executable .jar

## Execution

`mvn clean javafx:run`

or

`java -jar target/CYPath.jar` (or `java -jar target/CYPathStandalone.jar`) if you have the executable jar file

## Known Bugs/Issues to be fixed

- When you win the program stops. In a future update we may want to go to the main menu after winning a game
- Javadoc isn't complete so `mvn javadoc:jar` may generate errors
- For now entering an illegal file name for the save causes a crash
- We can't load a game
- Going to the menu and recreating a game cause an error with the Thread object used in CYPathFX
- The "continue Game Button" doesn't work (and isn't even display) for now
- If there are 4 players aligned and trapped in a 1x4 corridor with no fences left to be placed, then the game is stuck. It should be considered as a draw

## How to play

When you launch the application you can either press 'w' to open a JavaFX window or play in console mode by pressing 'c'.

### In the JavaFX window

- Click on the button to change the mode (placing fence or moving)
- Right click to change the fence orientation
- Left click on a cell to make a move or place a fence
- If a fence is red it's not a valid position and if it's green it can be placed
- The cells where you can move to are in a different color


### In the console mode display

`@` : represents a fence
`-` : represents a cell border (without fence)
`0, 1, 2, or 3` : represent the player (player 1, player 2, ...).

In this representation:
- the top left cell is at (0,0)
- the top right cell is at (8,0)
- the bottom left cell is at (0,8)
- the bottom right cell is at (8,8)

The cells coordinates are given on the left side and the top side of the board

To play you must write in the console your answer when a question is asked. We have the following questions/answers :

- "How many players do you want ? (2 or 4)"
    - Type either 2 or 4
- "What is your next action ? ('m' (move) or 'f' (place fence))"
    - "m" : Move your pawn to the position that you will provide just after
    - "f" : Place a fence to the position that you will provide just after along with its orientation
    - "s" : Save your current game to a file in resources/data/saves
- "Where do you want to go ?"
    - Here, you can only move to the positions given in the list after "Those are the possible moves you can do"
    - "X" : Give a number between 0 and 8. It's the x (horizontal) coordinate of the position where tou want to move your pawn
    - "Y" : Give a number between 0 and 8. It's the y (vertical) coordinate of the position where you want to move your pawn
- "What is the orientation of your fence ? (H(ORIZONTAL) or V(ERTICAL))" : 
    - Press H if you want to place a horizontal fence
    - Press V if you want to place a vertical one
- "Where do you want to put your fence ? (X,Y)"
    - "X" : Give a number between 0 and 8. It's the x (horizontal) coordinate of the position where tou want to move your fence
    - "Y" : Give a number between 0 and 8. It's the y (vertical) coordinate of the position where you want to move your pawn
    - Here we consider that (0,0) will place a fence going from (0,0) to (0,2) if it's vertical or (0,0) to (2,0) if it's horizontal. So the coordinates given here correspond to leftmost and uppermost point of the fence
- If any of the given input are incorrect we ask the same question to the user over and over again


## Documentation

Use `mvn javadoc:jar` to generate the Javadoc in `target/`.
Extract the jar archive and open index.html to read the documentation.

## Clean generated binaries and files

To clean the `target` folder, use `mvn clean`
