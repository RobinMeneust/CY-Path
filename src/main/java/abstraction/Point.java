package abstraction;

import presentation.CYPath;

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
     * @param p Point from where the new point os created
     */

    public Point(Point p) {
        this(0, 0);
        this.x = p.getX();
        this.y = p.getY();
    }

    /**
     * Get the abscissa of a point
     * 
     * @return X coordinate
     */

    public int getX() {
        return this.x;
    }

    /**
     * Get the ordinate of a point
     * 
     * @return Y coordinate
     */

    public int getY() {
        return this.y;
    }

    /**
     * Set the abscissa of a point
     * 
     * @param x X coordinate to change
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the ordinate of a point
     * 
     * @param y Y coordinate to change
     */

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the distance between two points
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

    /**
	 * Ask for a Point from the user, and return it.
	 * 
	 * @return Point chosen by the user
	 */

	public static Point choosePoint() {
		System.out.println();

        try {
            System.out.print("X : ");
            int x = Integer.parseInt(CYPath.scanner.next());
            System.out.println();
            System.out.print("Y : ");	
            int y = Integer.parseInt(CYPath.scanner.next());
            return new Point(x,y);
        } catch (NumberFormatException e) {
            throw e;
        }
	}

    /**
     * Rotate a point around a center
     * 
     * @param oldPoint Position of the point
     * @param center Position of the center of the rotation
     * @return Point rotated
     */

    public static Point rightRotation(Point oldPoint, Point center) {
        Point newPoint = new Point(oldPoint.getX()-center.getX(), oldPoint.getY()-center.getY());
        int temp = -1*newPoint.getX();
        newPoint.setX(newPoint.getY());
        newPoint.setY(temp);

        return Point.sum(newPoint, center);
    }

    /**
     * Sums 2 points by adding the x coordinates for x and the y coordinated for y
     * 
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Point got from the sum of point 1 and point 2
     */

    public static Point sum(Point p1, Point p2) {
        return new Point(p1.getX()+p2.getX(), p1.getY()+p2.getY());
    }
}
