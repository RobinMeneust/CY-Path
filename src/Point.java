public class Point {
	private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(){
        this(0,0);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

	public static int getDistance(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}

	public boolean equals(Object o) {
		if(o instanceof Point) {
			Point p = (Point) o;
			return (p.getX() == this.getX() && p.getY() == this.getY());
		}
		return false;
	}

    @Override
    public String toString(){
        return("("+this.getX()+","+this.getY()+")");
    }
}
