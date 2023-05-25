package abstraction;

/**
 * Fence placed on the board of the Quoridor's game
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Fence {
	/**
	 * Length of a fence
	 */
	private int length;
	/**
	 * Orientation of a fence
	 */
	private Orientation orientation;
	/**
	 * Fence's starting point
	 */
	private Point start;
	/**
	 * Fence's ending point
	 */
	private Point end;
	
	/**
	 * Create a Fence by giving all of its attributes
	 * 
	 * @param length Length of the fence
	 * @param orientation  Orientation of the fence
	 * @param start Starting point of the fence
	 * @param end Ending point of the fence
	 */

	public Fence(int length, Orientation orientation, Point start, Point end) {
		this.length = length;
		this.orientation = orientation;
		this.start = start;
		this.end = end;
	}

	/**
	 * Create a Fence by giving a length, an orientation and its starting point
	 * The ending point is deduced here.
	 * 
	 * @param length Length of the fence
	 * @param orientation  Orientation of the fence
	 * @param start Starting point of the fence
	 */

	public Fence(int length, Orientation orientation, Point start){
		this(length,orientation,new Point(start),new Point(start));
		if(orientation == Orientation.HORIZONTAL) {
			end.setX(start.getX()+length);
		} else {
			end.setY(start.getY()+length);
		}
	}

	/**
	 * Create a Fence by giving only its length
	 * It's the default constructor here
	 * 
	 * @param length Length of the fence
	 */

	public Fence(int length){
		this(length,Orientation.HORIZONTAL,new Point(0,0),new Point(length,0));
	}

	/**
	 * Create a Fence by giving only its orientation
	 * 
	 * @param orientation orientation of the fence
	 */

	public Fence(Orientation orientation){
		this(2, orientation, new Point(0,0),new Point(2,0));
	}

	/**
	 * Create a Fence by giving its orientation but also the starting and ending point
	 * 
	 * @param orientation orientation of the fence
	 * @param start start coordinates of the fence
	 * @param end end coordinates of the fence
	 */

	public Fence(Orientation orientation, Point start, Point end) {
		this(Point.getDistance(start, end), orientation, new Point(start), new Point(end));
	}

	
	/** 
	 * Get the length of the fence
	 * 
	 * @return Length of the fence
	 */

	public int getLength() {
		return length;
	}

	
	/** 
	 * Get the orientation of the fence
	 * 
	 * @return Orientation of the fence
	 */

	public Orientation getOrientation() {
		return orientation;
	}

	
	/** 
	 * Get the starting point of the fence
	 * 
	 * @return Starting point of the fence
	 */

	public Point getStart() {
		return start;
	}

	
	/** 
	 * Get the ending point of the fence
	 * 
	 * @return Ending point of the fence
	 */

	public Point getEnd() {
		return end;
	}

	
	/** 
	 * Set the starting point of the fence and change the ending point accordingly
	 * 
	 * @param start New starting point of the fence
	 */

	public void setStart(Point start) {
		this.start = start;

		switch (this.getOrientation()) {
			case HORIZONTAL:
				this.getEnd().setX(this.getStart().getX() + this.getLength());
				this.getEnd().setY(this.getStart().getY());
				break;
			case VERTICAL:
				this.getEnd().setX(this.getStart().getX());
				this.getEnd().setY(this.getStart().getY() + this.getLength());
				break;
			default:
				this.setEnd(this.getStart());
				break;
		}
	}

	
	/** 
	 * Set the orientation of the fence
	 * 
	 * @param orientation New orientation of the fence
	 */

	public void setOrientation(Orientation orientation){
		this.orientation = orientation;
	}
	
	/** 
	 * Set the orientation of the fence by converting the given String to an Orientation object
	 * 
	 * @param orientation New orientation of the fence
	 */

	public void setOrientation(String orientation){
		if (orientation.toUpperCase().matches("H(ORIZONTAL)?")){
			this.setOrientation(Orientation.HORIZONTAL);
		}else if(orientation.toUpperCase().matches("V(ERTICAL)?")){
			this.setOrientation(Orientation.VERTICAL);
		}
	}

	/**
	 * Set the end point of a fence
	 * @param end Ending of a fence
	 */
	public void setEnd(Point end) {
		this.end = end;
	}

	/**
	 * Returns a String representing the Fence in the following format:
	 * {
	 * length: LENGTH
	 * Orientation: ORIENTATION
	 * start: START
	 * end: END
	 * }
	 * 
	 * @return String representing the Fence.
	 */

	@Override
	public String toString(){
		return("{\nlength:"+this.length+",\n"+"Orientation:"+this.orientation+",\n"+"start:"+this.start+",\n"+"end:"+this.end+",\n"+"}");
	}
}
