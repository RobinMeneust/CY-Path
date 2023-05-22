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
