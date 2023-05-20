package abstraction;

/**
 * This class corresponds to all elements that are related to a point in a CY-PATH game.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Point {

    /**
     * State the Point's class attributes
     */

    private int x;
    private int y;

    /**
     * Create a point from his coordinates
     * 
     * @param x (int) : abscissa of a point
     * @param y (int) : ordinate of a point
     */

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a point without taking an argument
     */

    public Point() {
        this(0, 0);
    }

    /**
     * Create a point by taking another point as an argument
     * 
     * @param p (Point)
     */

    public Point(Point p) {
        this(0, 0);
        this.x = p.getX();
        this.y = p.getY();
    }

    /**
     * Accessor to recover the abscissa of a point
     * 
     * @return x (int)
     */

    public int getX() {
        return this.x;
    }

    /**
     * Accessor to recover the ordinate of a point
     * 
     * @return y (int)
     */

    public int getY() {
        return this.y;
    }

    /**
     * Accessor to assign the abscissa of a point
     * 
     * @param x (int)
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Accessor to assign the ordinate of a point
     * 
     * @param y (int)
     */

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Accessor to recover the distance between two points
     * 
     * @param p1 (Point) : First point coordinates
     * @param p2 (Point) : Second point coordinates
     * @return (int) : Distance between two points
     */

    public static int getDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    /**
     * Test the equality between two objects
     * 
     * @return (boolean) : true if the current object is like the object in parameter else false
     */

    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return (p.getX() == this.getX() && p.getY() == this.getY());
        }
        return false;
    }

    /**
     * Function to return an integer that uniquely represents the object on which the method is called
     * 
     * @return (int) an inteteger
     */

    public int hashCode() {
        int result = 11 + 83 * this.getX();
        result += 83 * result + this.getY();

        return result;
    }

    /**
     * Redefine 'toString' method of the Object class to display coordinates of a pawn
     * 
     * @return (String) : a text
     */

    @Override
    public String toString() {
        return ("(" + this.getX() + "," + this.getY() + ")");
    }
}
