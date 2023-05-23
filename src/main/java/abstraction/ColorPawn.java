package abstraction;

import javafx.scene.paint.Color;

/**
 * This enum represents the pawn possible colors.
 */

public enum ColorPawn {
    /**
     * Yellow color
     */
    YELLOW,
    /**
     * Blue color
     */
    BLUE,
    /**
     * Red color
     */
    RED,
    /**
     * Green color
     */
    GREEN;

    /**
     * Convert the colors of this enumeration to the ones from JavaFX.
     * The colors then can be used by graphical interface for the players.
     * @return Color from JavaFX associated to the color of this enumeration.
     */
    public Color toColorFX(){
        switch (this){
            case GREEN:
                return Color.GREEN;
            case YELLOW:
                return Color.YELLOW;
            case BLUE:
                return Color.BLUE;
            case RED:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    /**
     * Convert the colors of this enumeration to the ones from JavaFX to show which move is possible from the color of the current pawn.
     * It helps the players to know which pawn is color from its color.
     * @return Light color from JavaFX associated to the color of this enumeration.
     */
    public Color toColorPossibleMove(){
        switch (this){
            case GREEN:
                return Color.LIGHTGREEN;
            case YELLOW:
                return Color.LIGHTYELLOW;
            case BLUE:
                return Color.LIGHTBLUE;
            case RED:
                return Color.LIGHTPINK;
            default:
                return Color.BLACK;
        }
    }
}
