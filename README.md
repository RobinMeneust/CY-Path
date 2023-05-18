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

## Installation

- JavaFX must be installed (JDK)
- Maven is required

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

`java -jar target/CYPath.jar` if you have the executable jar file

## Known Bugs/Issues to be fixed

- In the JavaFX window, the movement of the player is not finished : in this version you have to click twice the button (used to change modes), to update the board, and a player can move twice and there is not pawn displayed
- Fences can't be placed yet

## How to play

For now this is mainly in console mode.
The JavaFX application window is opening, but it's just used to show the current progress of this project, it's still not finished.
As we are currently migrating from console mode to JavaFX, this version is not very ergonomic, since we need both the console and the application window.

In the console mode display:

`@` : represents a fence
`-` : represents a cell border (without fence)
`0, 1, 2, or 3` : represent the player (player 1, player 2, ...).

In this representation:
- the top left corner is at (0,0)
- the top right corner is at (8,0)
- the bottom left corner is at (0,8)
- the bottom right corner is at (8,8)

The cells coordinates are given on the left side and the top side of the board

To play you must write in the console your answer when question is asked to you. We have the following questions/answers :

NOTE: There might be some issues concerning how the user input is taken here (input not displayed, question displayed after the answer, ...), it will be fixed in a future update

- Select the next action you want to do by clicking the button and type 'yes' in the terminal
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


### Concerning the JavaFX application

The window opens after you entered the number of player (so when the game really start)
It's still under developement so all functionalities aren't available and it needs more testing

For now you can only:
- Choose your mode by clicking the button: Placing fences or moving your pawn
- See the possible moves your pawn can do
- Move the pawn by clicking an available cell, but in this version you have to click twice the button (used to change modes), to update the board, and a player can move twice and there is not pawn displayed
- See where a fence can be placed on the board by moving your mouse above the cells (green = it can be placed and red = it's not a valid position)


## Documentation

Use `mvn javadoc:jar` to generate the Javadoc in `target/`.
Extract the jar archive and open index.html to read the documentation.

## Clean generated binaries and files

To clean the `target` folder, use `mvn clean`
