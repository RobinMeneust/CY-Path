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
	
}
