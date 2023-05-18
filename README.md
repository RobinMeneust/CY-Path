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

Change `19` to your JDK version in prom.xml at the line :
`<java.version>19</java.version>`

Change `17.0.7` to your JavaFX version in prom.xml at the line :
`<javafx.version>17.0.7</javafx.version>`


## Compile and generate jar executable

`mvn clean prepare-package jar:jar package` to create the executables .jar (CYPathStandalone has the .java of JavaFX classes inside it, but we still haven't checked if it's really standalone)

or just `mvn clean prepare-package jar:jar` to create the not standalone executable .jar

## Execution

`mvn clean javafx:run`

or

`java -jar target/CYPath.jar` if you have the executable jar file

## Known Bugs/Issues to be fixed

## How to play

For now there is only a console mode:
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

- "What is your next action ? ('m' (move) or 'f' (place fence))"
    - Press m if you want to move your pawn
    - Press f if you want to place a fence
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
- If any of the given input are incorrect the


## Documentation

Use `mvn javadoc:jar` to generate the Javadoc in `target/`.
Extract the jar archive and open index.html to read the documentation.

## Clean generated binaries and files

To clean the `target` folder, use `mvn clean`
