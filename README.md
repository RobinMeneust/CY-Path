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

- JavaFX must be installed (choose the correct version according to your system (Windows, Linux...))
- (on Windows : Add it to your PATH environment variable if it isn't already in it)
- In build.xml, change the value of javafx.dir (at the beginning of the file) to the location of your JavaFX folder

## Compile

`ant compile_src_files` to compile

## Execution

`ant create_run_jar` to create the executable .jar and then `java --module-path="JAVAFX_PATH" --add-modules javafx.controls -jar CY_TECH_Project.jar` to run it, but replace JAVAFX_PATH by the path to your JavaFX bin folder

or

`ant` or `ant execute_run_jar`

## Documentation

`ant javadoc` and then go to the folder doc/ and open index.html

