public class Fence {
	private int length;
	private Orientation orientation;
	private Point start;
	private Point end;

	public Fence(int length, Orientation orientation, Point start, Point end) {
		this.length = length;
		this.orientation = orientation;
		this.start = start;
		this.end = end;
	}

	public Fence(int length){
		this(length,Orientation.HORIZONTAL,new Point(0,0),new Point(2,0));
	}

	public int getLength() {
		return length;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point start){
		switch (this.getOrientation()) {
			case HORIZONTAL:
				this.end.setX(start.getX() + this.getLength());
				this.end.setY(start.getY());
				break;
			case VERTICAL:
				this.end.setX(start.getX());
				this.end.setY(start.getY() + this.getLength());
				break;
			default:
				this.end = start;
				break;
		}
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public void setOrientation(Orientation orientation){
		this.orientation = orientation;
	}
	public void setOrientation(String orientation){
		if (orientation.toUpperCase().matches("H(ORIZONTALE)?")){
			this.setOrientation(Orientation.HORIZONTAL);
		}else if(orientation.toUpperCase().matches("V(ERTICAL)?")){
			this.setOrientation(Orientation.VERTICAL);
		}
	}

	@Override
	public String toString(){
		return("{\nlenght:"+this.length+",\n"+"Orientation:"+this.orientation+",\n"+"start:"+this.start+",\n"+"end:"+this.end+",\n"+"}");
	}
}
